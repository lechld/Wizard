package at.aau.edu.wizards.api.model

import at.aau.edu.wizards.model.GameModelCard
import kotlin.random.Random

class GameModelInstance() {
    //TODO Check variables
    private val playerList = ArrayList<GameModelPlayer>()
    private val dealtCards = ArrayList<GameModelCard>()
    private var activePlayer = 0; //which player can make a play
    private var turn = 0 //which turn is it
    private var dealer = 0 //who is starting
    private val board = GameModelBoard(this)


    //TODO Add game functions
    fun initGame() {
        if (turn != 0) {
            throw Exception("Failed to initialize game: Game already running!")
        }
        initFirstDealer()
        nextTurn()
    }

    private fun initFirstDealer() {
        dealer = Random.nextInt(0, playerList.size)
    }

    private fun getRandomCard(player: GameModelPlayer): GameModelCard {
        return GameModelCard(Random.nextInt(0, 14), Random.nextInt(1, 4), player)
    }

    private fun dealCards() {
        dealtCards.clear()
        var card: GameModelCard
        for (player in 0 until playerList.size) {
            for (unused in 1..turn) {
                if (dealtCards.size == 60) {
                    throw Exception("Failed to deal cards: No further cards left, all in play!")
                }
                //TODO find a more resource efficient way to handle this (currently just random tries)
                do {
                    card = getRandomCard(playerList[player])
                } while (dealtCards.contains(card))
                playerList[player].addCard(card)
                dealtCards.add(card)
            }
        }
    }

    fun addPlayerHuman() {
        if (turn != 0) {
            throw Exception("Failed to add Player Human: Game is already in progress, initialization is over!")
        }
        playerList.add(GameModelPlayer.GameModelPlayerHuman())
        return
    }

    fun addPlayerCPU() {
        if (turn != 0) {
            throw Exception("Failed to add Player CPU: Game is already in progress, initialization is over!")
        }
        playerList.add(GameModelPlayer.GameModelPlayerCPU())
        return
    }

    fun cardPlayed() {
        if (activePlayer == playerList.size) {
            activePlayer = 0
        } else {
            activePlayer++
        }
        if (board.cardsInPlay() == playerList.size) {
            if (playerList[0].getCards().isEmpty()) {
                scorePlayer()
                board.clearBoardTurn()
                nextTurn()
            } else {
                board.clearBoardRound()
            }
        }
    }

    private fun nextTurn() {
        if (this.turn in 0..9) {
            turn++
            dealCards()
            scorePlayer()
        } else {
            //TODO end game
        }
    }

    private fun scorePlayer() {
        for (player in playerList) {
            player.score()
        }
    }
}