package at.aau.edu.wizards.gameModel

import kotlin.random.Random

class GameModelConfig(private val parent: GameModel) : GameModelConfigInterface {

    private val trumps = ArrayList<String>()
    private val dealer = GameModelCardDealer()
    private var player = 1

    override fun createConfig(
        numberOfPlayerHuman: Int,
        numberOfPlayerCPU: Int
    ): GameModelResult<Unit> {
        if (6 < numberOfPlayerHuman + numberOfPlayerCPU || numberOfPlayerHuman + numberOfPlayerCPU < 3) {
            return GameModelResult.Failure(Exception("Failed to create config: Player amount must be between 3 and 6!"))
        }
        for (human in 1..numberOfPlayerHuman) {
            parent.listOfPlayers.add(GameModelPlayer((parent.listOfPlayers.size), 0))
        }
        for (cpu in 1..numberOfPlayerCPU) {
            parent.listOfPlayers.add(GameModelPlayer((parent.listOfPlayers.size), 1))
        }
        for (round in 1..10) {
            for (player in 0 until parent.listOfPlayers.size) {
                var i = 1 //we get more coverage doing this than with for loop
                while (i++ <= round) {
                    parent.listOfPlayers[player].addCardToPlayerStack(
                        dealer.dealCardInSet(
                            player
                        )
                    )
                }
            }
            if (round < 10) {
                val trump = buildString {
                    append(Random.nextInt(0, 14).toChar())
                    append(Random.nextInt(1, 4).toChar())
                    append(6.toChar())
                }
                parent.receiveMove(trump)
                trumps.add(trump)
            } else {
                val trump = buildString {
                    append(0.toChar())
                    append(0.toChar())
                    append(6.toChar())
                }
                parent.receiveMove(trump)
                trumps.add(trump)
            }
            dealer.resetSet()
        }
        parent.receiveMove(buildString { append(1.toChar()) })
        return GameModelResult.Success(Unit)
    }

    override fun getConfig(): GameModelResult<String> {
        while (parent.listOfPlayers[player].isCPU == 1) {
            player++
            if (player >= parent.listOfPlayers.size) {
                return GameModelResult.Failure(Exception("Failed to get config: Player is exceeding player amount specified in config!"))
            }
        }
        return GameModelResult.Success(buildString {
            append(player++.toChar())
            for (player in 0 until parent.listOfPlayers.size) {
                append(parent.listOfPlayers[player].getString())
            }
            for (trump in trumps) {
                append(trump)
            }
        }
        )
    }

}