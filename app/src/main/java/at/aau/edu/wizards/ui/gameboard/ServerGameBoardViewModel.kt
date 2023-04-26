package at.aau.edu.wizards.ui.gameboard

import at.aau.edu.wizards.api.Server

class ServerGameBoardViewModel(
    server: Server,
) : GameBoardViewModel(messageReceiver = server, messageSender = server)