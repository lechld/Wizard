package at.aau.edu.wizards.gameModel

import kotlin.random.Random

// FIXME why does cpu need a gamemodelplayer in most methods
class GameModelCpu(seed: Int, private val rules: GameModelRules) {
    private val random = Random(seed)

    fun getGuess(player: GameModelPlayer): Int {
        var guess = 0
        for (card in player.cards) {
            if (willWinTotal(card, player)) {
                guess++
            }
        }

        return guess + ((player.cards.size - guess) / 8)
    }


    fun getMove(player: GameModelPlayer): GameModelCard {
        val playableCards = ArrayList<GameModelCard>()
        for (card in player.cards) {
            if (willWin(
                    card,
                    player
                ) && player.guesses[player.guesses.lastIndex] > rules.getAmountWon(player.id)
            ) {
                return card
            } else if (legalCard(rules.winningCard, card, player)) {
                playableCards.add(card)
            }
        }
        if (playableCards.isEmpty()) {
            return GameModelCard.NoCard
        } else if (playableCards.lastIndex == 0) {
            return playableCards[0]
        }
        return playableCards[random.nextInt(0, playableCards.lastIndex)]
    }

    private fun willWinTotal(card: GameModelCard, player: GameModelPlayer): Boolean {
        for (playerId in 0 until rules.players.size) {
            if (playerId == player.id) {
                continue
            }
            for (cardNew in rules.players[playerId].cards) {
                if (legalCard(card, cardNew, rules.players[playerId]) && cardBeatsCard(
                        card,
                        cardNew
                    )
                ) {
                    return false
                }
            }
        }
        return true
    }

    private fun willWin(card: GameModelCard, player: GameModelPlayer): Boolean {
        if (legalCard(rules.winningCard, card, player) && cardBeatsCard(rules.winningCard, card)) {
            for (playerId in player.id + 1 until rules.players.size) {
                for (cardNew in rules.players[playerId].cards) {
                    if (legalCard(card, cardNew, rules.players[playerId]) && cardBeatsCard(
                            card,
                            cardNew
                        )
                    ) {
                        return false
                    }
                }
            }
        } else {
            return false
        }
        return true
    }

    private fun cardBeatsCard(cardToBeat: GameModelCard, card: GameModelCard): Boolean {
        return when (cardToBeat) {
            is GameModelCard.Jester -> {
                when (card) {
                    is GameModelCard.Jester -> false
                    else -> true
                }
            }
            GameModelCard.NoCard -> true
            is GameModelCard.Normal -> {
                when (card) {
                    is GameModelCard.Normal -> {
                        when (val trump = rules.trump) {
                            is GameModelCard.Normal -> {
                                (cardToBeat.color == card.color && cardToBeat.value < card.value) || (cardToBeat.color != card.color && card.color == trump.color)
                            }
                            is GameModelCard.Wizard -> {
                                (cardToBeat.color == card.color && cardToBeat.value < card.value) || (cardToBeat.color != card.color && card.color == trump.color)
                            }
                            else -> {
                                cardToBeat.color == card.color && cardToBeat.value < card.value
                            }
                        }
                    }
                    is GameModelCard.Wizard -> true
                    else -> false
                }
            }
            is GameModelCard.Wizard -> false
        }
    }

    private fun legalCard(
        cardToBeat: GameModelCard,
        card: GameModelCard,
        player: GameModelPlayer
    ): Boolean {
        return when (cardToBeat) {
            is GameModelCard.Jester -> true
            GameModelCard.NoCard -> true
            is GameModelCard.Normal -> {
                when (card) {
                    is GameModelCard.Normal -> {
                        (cardToBeat.color == card.color || !player.hasColor(cardToBeat.color))
                    }
                    else -> true
                }
            }
            is GameModelCard.Wizard -> true
        }
    }
}