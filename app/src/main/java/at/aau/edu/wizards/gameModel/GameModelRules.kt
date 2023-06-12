package at.aau.edu.wizards.gameModel

import kotlinx.coroutines.delay

class GameModelRules(
    val players: ArrayList<GameModelPlayer>,
    val id: Int,
    private val cardDealer: GameModelDealer,
    private val parent: GameModel,
    seed: Int
) {

    private val cpu = GameModelCpu(seed, this)
    val board = ArrayList<GameModelCard>()
    private val wins = ArrayList<Int>()
    var trump: GameModelCard = GameModelCard.NoCard
        private set
    var round = 0
        private set
    var currentPlayer = 0
        private set
    private var dealer = 0
    private var winningPlayer = 0
    var winningCard: GameModelCard = GameModelCard.NoCard
    var wantsGuess = false
    var lastPlayerWon = 0
    var lastCardWon: GameModelCard = GameModelCard.NoCard
    var numberOfSet = 0

    fun init() {
        if (round == 0) {
            round++
            for (player in players) {
                player.dealCards(round)
            }
            trump = cardDealer.dealCardInSet()
            dealer = 0
            currentPlayer = 0
            cardDealer.resetSet()
            for (player in players) {
                if (!player.isHuman) {
                    player.guesses.add(cpu.getGuess(player.id))
                }
            }
            getGuess()
        }
    }

    private fun getGuess() {
        wantsGuess = true
    }

    private suspend fun nextRound() {
        for (player in players) {
            player.score(getAmountWon(player.id))
        }
        wins.clear()
        round++
        if (round == 11) {
            return endGame()
        }
        for (player in players) {
            player.dealCards(round)
        }
        trump = cardDealer.dealCardInSet()
        if (++dealer >= players.size) {
            dealer = 0
        }
        currentPlayer = dealer
        cardDealer.resetSet()
        for (player in players) {
            if (!player.isHuman) {
                player.guesses.add(cpu.getGuess(player.id))
            }
        }
        getGuess()
        parent.listener.update()
    }


    private fun endGame() {
        parent.sendMessage(END_COMMAND)
    }

    suspend fun playCard(card: GameModelCard) {
        if (card is GameModelCard.NoCard) {
            throw (Exception("Failed to play card: It is of type NoCard!"))
        } else if (winningCard is GameModelCard.NoCard) {
            addWinningCard(card)
        } else {
            when (card) {
                is GameModelCard.Jester -> {
                    addJester(card)
                }
                is GameModelCard.Wizard -> {
                    addWizard(card)
                }
                else -> {
                    checkNormal(card)
                }
            }
        }
    }

    private suspend fun checkNormal(card: GameModelCard) {
        when (winningCard) {
            is GameModelCard.Normal -> {
                if ((winningCard as GameModelCard.Normal).color == (card as GameModelCard.Normal).color
                    || !players[currentPlayer].hasColor(
                        (winningCard as GameModelCard.Normal).color
                    )
                ) {
                    addNormal(card)
                }
            }
            else -> {
                addNormal(card)
            }
        }
    }

    private suspend fun addJester(card: GameModelCard) {
        addLoosingCard(card)
    }

    private suspend fun addWizard(card: GameModelCard) {
        when (winningCard) {
            is GameModelCard.Wizard -> {
                addLoosingCard(card)
            }
            else -> {
                addWinningCard(card)
            }
        }
    }

    private suspend fun addNormal(card: GameModelCard) {
        when (winningCard) {
            is GameModelCard.Jester -> {
                addWinningCard(card)
            }
            is GameModelCard.Wizard -> {
                addLoosingCard(card)
            }
            else -> {
                if ((card as GameModelCard.Normal).color == (winningCard as GameModelCard.Normal).color) {
                    if (card.value > (winningCard as GameModelCard.Normal).value) {
                        addWinningCard(card)
                    } else {
                        addLoosingCard(card)
                    }
                } else {
                    addNormalCompareToTrump(card)
                }
            }
        }
    }

    private suspend fun addNormalCompareToTrump(card: GameModelCard) {
        when (trump) {
            is GameModelCard.Normal -> {
                if ((card as GameModelCard.Normal).color == (trump as GameModelCard.Normal).color) {
                    addWinningCard(card)
                } else {
                    addLoosingCard(card)
                }
            }
            is GameModelCard.Wizard -> {
                if ((card as GameModelCard.Normal).color == (trump as GameModelCard.Wizard).color) {
                    addWinningCard(card)
                } else {
                    addLoosingCard(card)
                }
            }
            else -> {
                addLoosingCard(card)
            }
        }
    }

    private suspend fun addWinningCard(card: GameModelCard) {
        winningPlayer = currentPlayer
        if (winningCard !is GameModelCard.NoCard) {
            board.add(winningCard)
        }
        winningCard = card
        players[currentPlayer].cards.remove(card)
        nextPlayer()
    }

    private suspend fun addLoosingCard(card: GameModelCard) {
        board.add(card)
        players[currentPlayer].cards.remove(card)
        nextPlayer()
    }

    private suspend fun nextPlayer() {
        if (++currentPlayer >= players.size) {
            currentPlayer = 0
        }
        parent.listener.update()
        if (currentPlayer == dealer) {
            nextSet()
        } else if (!players[currentPlayer].isHuman) {
            getCpuToPlay()
        }
    }

    private suspend fun nextSet() {
        numberOfSet++
        lastCardWon = winningCard
        lastPlayerWon = winningPlayer
        parent.listener.update()
        wins.add(winningPlayer)
        winningCard = GameModelCard.NoCard
        board.clear()
        if (players[0].cards.isEmpty()) {
            nextRound()
        } else if (!players[currentPlayer].isHuman) {
            parent.listener.update()
            getCpuToPlay()
        }
    }

    fun currentPlayerOwns(card: GameModelCard): Boolean {
        return players[currentPlayer].cards.any { it == card }
    }

    fun localPlayerOwns(card: GameModelCard): Boolean {
        return players[id].cards.any { it == card }
    }

    fun getAmountWon(id: Int): Int {
        var amount = 0
        for (win in wins) {
            if (id in win..win) { //this is essentially a if id == win operation, but it is written in this form because id == win does not give complete coverage
                amount++
            }
        }
        return amount
    }

    fun everyoneHasGuessed(): Boolean {
        for (player in players) {
            if (player.guesses.size < round) {
                return false
            }
        }
        return true
    }

    suspend fun addGuess(guess: Int) {
        if (players[getPlayerIdFromGuessInInt(guess)].guesses.size < round) {
            players[getPlayerIdFromGuessInInt(guess)].guesses.add(getGuessValueFromGuessInInt(guess))
        }
        if (getPlayerIdFromGuessInInt(guess) == id) {
            wantsGuess = false
        }
        if (everyoneHasGuessed() && !players[currentPlayer].isHuman) {
            parent.listener.update()
            getCpuToPlay()
        }
    }

    private fun getPlayerIdFromGuessInInt(guess: Int): Int {
        return (guess - 60) / 11
    }

    private fun getGuessValueFromGuessInInt(guess: Int): Int {
        return (guess - 60) % 11
    }

    private suspend fun getCpuToPlay() {
        delay(1000)
        parent.receiveMessage(cpu.getMove(currentPlayer).getString())
    }
}