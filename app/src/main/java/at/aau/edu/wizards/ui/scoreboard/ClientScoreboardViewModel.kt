package at.aau.edu.wizards.ui.scoreboard

import at.aau.edu.wizards.api.Client


class ClientScoreboardViewModel(
    client: Client,
) : ScoreboardViewModel(messageReceiver = client, messageSender = client)