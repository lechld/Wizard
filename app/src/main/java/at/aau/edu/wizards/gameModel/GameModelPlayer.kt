package at.aau.edu.wizards.gameModel

class GameModelPlayer(override val id: Int, val isCPU: Int) : GameModelPlayerInterface {

    override val cards = ArrayList<GameModelCard>()
    private val currentCards = ArrayList<GameModelCard>()
    private var guess:Int? = null
    private val scores = ArrayList<Int>()

    override fun getString(): String {
        return buildString {
            append(15.toChar())
            append(isCPU.toChar())
            append(id.toChar())
            for (card in 0 until cards.size) {
                append(cards[card].getString())
            }
        }
    }

    fun dealHand(turn:Int): GameModelResult<Unit> {
        if (turn > 10) {
            return GameModelResult.Failure(Exception("Failed to deal hand: Trying to go over the turn limit of 10!"))
        } else if(currentCards.isNotEmpty()) {
            return GameModelResult.Failure(Exception("Failed to deal hand: There are still cards in play!"))
        }
        var pos = 0
        for (skip in 1..turn){
            pos += skip
        }
        for (card in 0 until turn) {
            currentCards.add(cards[pos + card])
        }
        pos += turn
        return GameModelResult.Success(Unit)
    }

    fun cardsContain(hash: String): Boolean {
        for (card in 0 until currentCards.size) {
            if(currentCards[card].getString()==hash){
                return true
            }
        }
        return false
    }

    fun cardsContainColor(color:Int): Boolean {
        for (card in 0 until currentCards.size) {
            if(currentCards[card].color==color){
                return true
            }
        }
        return false
    }

    fun removeCardFromHand(card :GameModelCard){
        //TODO ERROR HANDLING
        for(index in 0 until currentCards.size){
            if(currentCards[index] == card){
                currentCards.removeAt(index)
            }
        }
    }

    fun cardsEmpty(): Boolean{
        if(currentCards.isEmpty()){
            return true
        }
        return false
    }

    fun getGuess(){
        //TODO implement and call GameModel sendMove - currently mock function
        guess = 1
    }

    fun setGuess(){
        //TODO implement and call from GameModel reciveMove
    }

    fun score(amountWon : Int){
        //TODO add Error catch for NULL CASE
        if (guess!! == amountWon) {
            scores.add(20 + (amountWon * 10))
        } else {
            scores.add((amountWon * 10) - ((guess!! - amountWon) * 10))
        }
        guess = null
    }
}