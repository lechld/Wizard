package at.aau.edu.wizards.ownHand

var cardList =  mutableListOf<Cards>()

class Cards (
    var card: Int,
    var id: Int? = cardList.size
)