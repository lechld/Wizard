package at.aau.edu.wizards.gameModel

import kotlin.random.Random

class GameModelDealer(seed: Int) {
    private val random = Random(seed)
    private val set = ArrayList<Int>()

    fun dealCardInSet(winniePuh: Boolean): GameModelCard {
        if (set.size > 59) { //Check if all cards already dealt
            return GameModelCard.NoCard
        }
        var hash = random.nextInt(
            0,
            59
        ) //get a random card (there are 60 cards so we get a random value between 0 and 59, each representing a unique card (which we can get with some math juggling)
        while (set.contains(hash)) { //make sure card is not already dealt
            hash--
            if (hash < 0) {
                hash = 59
            }
        }
        if (winniePuh) {
            if (random.nextBoolean() && random.nextBoolean()) {
                if (hash < 15) {
                    for (card in 14 downTo 0) {
                        if (set.contains(card)) {
                            continue
                        }
                        hash = card
                        break
                    }
                } else if (hash < 30) {
                    for (card in 29 downTo 15) {
                        if (set.contains(card)) {
                            continue
                        }
                        hash = card
                        break
                    }
                } else if (hash < 45) {
                    for (card in 44 downTo 30) {
                        if (set.contains(card)) {
                            continue
                        }
                        hash = card
                        break
                    }
                } else {
                    for (card in 59 downTo 45) {
                        if (set.contains(card)) {
                            continue
                        }
                        hash = card
                        break
                    }
                }
            } else if (random.nextBoolean()) {
                if (!set.contains(0)) {
                    hash = 0
                } else if (!set.contains(15)) {
                    hash = 15
                } else if (!set.contains(30)) {
                    hash = 30
                } else if (!set.contains(45)) {
                    hash = 45
                }
            }
        }
        set.add(hash) //set card as already dealt
        return getCardFromHash(hash)
    }

    fun getCardFromHash(hash: Int): GameModelCard {
        return if (hash % 15 == 0) {
            GameModelCard.Jester(getColorFromHash(hash))
        } else if (hash % 15 == 14) {
            GameModelCard.Wizard(getColorFromHash(hash))
        } else {
            GameModelCard.Normal(getColorFromHash(hash), hash % 15)
        }
    }

    private fun getColorFromHash(hash: Int): GameModelCard.Color {
        return when (hash / 15) {
            0 -> GameModelCard.Color.Blue
            1 -> GameModelCard.Color.Green
            2 -> GameModelCard.Color.Orange
            else -> GameModelCard.Color.Red
        }
    }

    fun resetSet() {
        set.clear()
    }

}