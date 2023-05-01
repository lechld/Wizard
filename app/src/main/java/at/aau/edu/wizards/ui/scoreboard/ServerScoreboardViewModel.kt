package at.aau.edu.wizards.ui.scoreboard

import at.aau.edu.wizards.api.Server

class ServerScoreboardViewModel(
    server: Server,
    amountCpu: Int,
) : ScoreboardViewModel(messageReceiver = server, messageSender = server)