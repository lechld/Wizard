package at.aau.edu.wizards.gameModel

class GameModelRules(
    private val players: ArrayList<GameModelPlayer>,
    val id: Int,
    private val cardDealer: GameModelDealer,
    private val parent: GameModel
) {

    val board = ArrayList<GameModelCard>()
    private val wins = ArrayList<Int>()
    var trump: GameModelCard = GameModelCard.NoCard
        private set
    private var round = 0
    var currentPlayer = 0
        private set
    private var dealer = 0
    private var winningPlayer = 0
    var winningCard: GameModelCard = GameModelCard.NoCard

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
        }
    }

    private fun nextRound() {
        if (round == 10) {
            return endGame()
        }
        for (player in players) {
            player.score(getAmountWon(player.id))
        }
        wins.clear()
        round++
        for (player in players) {
            player.dealCards(round)
        }
        trump = cardDealer.dealCardInSet()
        if (++dealer >= players.size) {
            dealer = 0
        }
        currentPlayer = dealer
        cardDealer.resetSet()
    }


    private fun endGame() {
        parent.sendMessage("EndGame")
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
        players[currentPlayer].cards.remove(card)
        nextPlayer()
    }

    private fun addLoosingCard(card: GameModelCard) {
        board.add(card)
        players[currentPlayer].cards.remove(card)
        nextPlayer()
    }

    private fun nextPlayer() {
        if (++currentPlayer >= players.size) {
            currentPlayer = 0
        }
        if (currentPlayer == dealer) {
            nextSet()
        }
    }

    private fun nextSet() {
        wins.add(winningPlayer)
        winningCard = GameModelCard.NoCard
        board.clear()
        if (players[0].cards.isEmpty()) {
            nextRound()
        }
    }

    fun currentPlayerOwns(card: GameModelCard): Boolean {
        return players[currentPlayer].cards.any {it == card}
    }

    fun localPlayerOwns(card: GameModelCard): Boolean {
        return players[id].cards.any {it == card}
    }

    private fun getAmountWon(id: Int): Int {
        var amount = 0
        for (win in wins) {
            if (id in win..win) { //this is essentially a if id == win operation, but it is written in this form because id == win does not give complete coverage
                amount++
            }
        }
        return amount
    }
}