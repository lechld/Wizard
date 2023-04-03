package at.aau.edu.wizards.gameModel

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class GameModelConfigUnitTest {

    private val test = GameModelUnitTest()

    @Test
    fun configTest() {
        var parent = GameModel()
        var config = GameModelConfig(parent)

        assertFalse(test.resultUnit(config.createConfig(0, 0)))
        assertFalse(test.resultUnit(config.createConfig(1, 0)))
        assertFalse(test.resultUnit(config.createConfig(2, 0)))
        assertFalse(test.resultUnit(config.createConfig(1, 1)))
        assertFalse(test.resultUnit(config.createConfig(0, 2)))
        assertFalse(test.resultUnit(config.createConfig(0, 1)))
        assertFalse(test.resultUnit(config.createConfig(0, 7)))
        assertFalse(test.resultUnit(config.createConfig(1, 6)))
        assertFalse(test.resultUnit(config.createConfig(2, 5)))
        assertFalse(test.resultUnit(config.createConfig(3, 4)))
        assertFalse(test.resultUnit(config.createConfig(4, 3)))
        assertFalse(test.resultUnit(config.createConfig(5, 2)))
        assertFalse(test.resultUnit(config.createConfig(6, 1)))
        assertFalse(test.resultUnit(config.createConfig(7, 0)))

        parent = GameModel()
        config = GameModelConfig(parent)
        assert(test.resultUnit(config.createConfig(0, 3)))

        parent = GameModel()
        config = GameModelConfig(parent)
        assert(test.resultUnit(config.createConfig(1, 2)))

        parent = GameModel()
        config = GameModelConfig(parent)
        assert(test.resultUnit(config.createConfig(2, 1)))

        parent = GameModel()
        config = GameModelConfig(parent)
        assert(test.resultUnit(config.createConfig(3, 0)))

        parent = GameModel()
        config = GameModelConfig(parent)
        assert(test.resultUnit(config.createConfig(0, 6)))

        parent = GameModel()
        config = GameModelConfig(parent)
        assert(test.resultUnit(config.createConfig(6, 0)))

        parent = GameModel()
        config = GameModelConfig(parent)
        assert(test.resultUnit(config.createConfig(5, 1)))

        parent = GameModel()
        config = GameModelConfig(parent)
        assert(test.resultUnit(config.createConfig(1, 5)))

        assertFalse(test.resultString(config.getConfig()))

    }
}