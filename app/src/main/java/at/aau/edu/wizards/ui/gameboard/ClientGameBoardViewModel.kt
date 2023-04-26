package at.aau.edu.wizards.ui.gameboard

import at.aau.edu.wizards.api.Client

class ClientGameBoardViewModel(
    client: Client,
) : GameBoardViewModel(messageReceiver = client, messageSender = client)