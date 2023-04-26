package at.aau.edu.wizards.ui.gameboard

import at.aau.edu.wizards.api.Server

class ServerGameBoardViewModel(
    server: Server,
    amountCpu: Int,
) : GameBoardViewModel(messageReceiver = server, messageSender = server)