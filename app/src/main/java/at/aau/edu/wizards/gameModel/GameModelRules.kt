package at.aau.edu.wizards.gameModel

class GameModelRules(
    private val players: ArrayList<GameModelPlayer>,
    val id: Int,
    private val cardDealer: GameModelDealer
) {

    private val board = ArrayList<GameModelCard>()
    val wins = ArrayList<Int>()
    private var trump: GameModelCard = GameModelCard.NoCard
    private var round = 0
    private var currentPlayer = 0
    private var dealer = 0
    private var winningPlayer = 0
    private var winningCard: GameModelCard = GameModelCard.NoCard

    fun nextRound() {
        board.clear()
        if (round == 10) {
            return endGame()
        }
        round++
        for (player in players) {
            player.dealCards(round)
        }
        trump = cardDealer.dealCardInSet()
        dealer++
        currentPlayer = dealer
        cardDealer.resetSet()
    }


    private fun endGame() {
        //TODO
    }

    fun playCard(card: GameModelCard) {
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

    private fun checkNormal(card: GameModelCard) {
        when (winningCard) {
            is GameModelCard.Normal -> {
                if ((winningCard as GameModelCard.Normal).color == (card as GameModelCard.Normal).color || !players[currentPlayer].hasColor(
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

    private fun addJester(card: GameModelCard) {
        addLoosingCard(card)
    }

    private fun addWizard(card: GameModelCard) {
        when (winningCard) {
            is GameModelCard.Wizard -> {
                addLoosingCard(card)
            }
            else -> {
                addWinningCard(card)
            }
        }
    }

    private fun addNormal(card: GameModelCard) {
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

    private fun addNormalCompareToTrump(card: GameModelCard) {
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

    private fun addWinningCard(card: GameModelCard) {
        winningPlayer = currentPlayer
        if (winningCard !is GameModelCard.NoCard) {
            board.add(winningCard)
        }
        winningCard = card
        nextSet()
    }

    private fun addLoosingCard(card: GameModelCard) {
        board.add(card)
        nextSet()
    }

    private fun nextSet() {
        wins.add(winningPlayer)
        if (++currentPlayer == players.size) {
            currentPlayer = 0
        }
        winningCard = GameModelCard.NoCard
        if (currentPlayer == dealer) {
            for (player in players) {
                player.score(getPlayerWins(player.id))
            }
            nextRound()
        }
    }

    private fun getPlayerWins(id: Int): Int {
        var amount = 0
        for (win in wins) {
            if (win == id) {
                amount++
            }
        }
        return amount
    }

    fun currentPlayerOwns(card: GameModelCard): Boolean {
        if (players[currentPlayer].cards.contains(card)) {
            return true
        }
        return false
    }

    fun localPlayerOwns(card: GameModelCard): Boolean {
        if (players[id].cards.contains(card)) {
            return true
        }
        return false
    }

    fun getActivePlayer(): Int {
        return currentPlayer
    }

    fun getTrump(): GameModelCard {
        return trump
    }
}