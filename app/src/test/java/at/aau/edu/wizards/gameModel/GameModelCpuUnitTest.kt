package at.aau.edu.wizards.gameModel

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

class GameModelCpuUnitTest {
    private val viewModel = null

    @Test
    fun metric() = runTest {
        //I think it makes more sense to test for a metric here, since the specific cpu implementation might change in future versions. We should just make sure it stays at a considerable level, namely staying better than previous versions.
        val limit = 1000
        var scores = 0
        var pZero = 0

        for (i in 0..limit) {
            val ran = Random(420420)
            val model = GameModel(viewModel)
            model.receiveMessage(buildString {
                append(0.toChar())
                append(1.toChar())
                append(5.toChar())
                append(i.toString())
            })
            for (k in 1..10) {

                val guess = ran.nextInt(0, k)
                model.receiveMessage(buildString {
                    append((60 + guess).toChar())
                })
                for (j in 1..k) {
                    var sent = false
                    for (card in model.listener.getHandOfPlayer(0)) {
                        if (!sent && model.sendMessage(card.getString())) {
                            model.receiveMessage(card.getString())
                            sent = true
                        }
                    }
                    if (!sent) {
                        continue
                    }
                }
            }
            var temp = 0
            for (k in 1..5) {
                temp += model.listener.getCurrentScoreOfPlayer(k)
            }
            pZero += model.listener.getCurrentScoreOfPlayer(0)
            scores += temp / 5

        }

        //Assertions.assertEquals(1, pZero / limit) //To see the value of random guesses
        //Assertions.assertEquals(1, scores / limit) //To see the value of cpu guesses

        Assertions.assertTrue(pZero < scores)
        Assertions.assertTrue(scores >= 157) //if you improve cpu - measure performance by de-commenting above code and adjust to new standard
    }

    @Test
    fun noCard() = runTest {
        val model = GameModel(viewModel)
        val dealer = GameModelDealer(420420)
        val players = ArrayList<GameModelPlayer>()
        players.add(GameModelPlayer(0, dealer, true, 1, "test"))
        players.add(GameModelPlayer(1, dealer, false, 1, "test"))
        players.add(GameModelPlayer(2, dealer, false, 1, "test"))
        val rules = GameModelRules(players, 0, dealer, model, 420420)

        val cpu = GameModelCpu(420420, rules)

        Assertions.assertEquals(GameModelCard.NoCard, cpu.getMove(players[1]))

        for (player in players) {
            player.cards.add(GameModelCard.NoCard)
        }

        Assertions.assertEquals(0, cpu.getGuess(players[1]))

        Assertions.assertEquals(GameModelCard.NoCard, cpu.getMove(players[1]))

        players[0].cards.add(GameModelCard.Jester(GameModelCard.Color.Orange))
        assertThrows<Exception> { rules.playCard(GameModelCard.Jester(GameModelCard.Color.Orange)) }
        Assertions.assertEquals(0, cpu.getGuess(players[1]))
        Assertions.assertEquals(GameModelCard.NoCard, cpu.getMove(players[1]))
    }
    
}