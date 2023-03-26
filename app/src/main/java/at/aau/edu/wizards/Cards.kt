package at.aau.edu.wizards

var cardList =  mutableListOf<Cards>()

class Cards (
    var card: Int,
    var id: Int? = cardList.size
)