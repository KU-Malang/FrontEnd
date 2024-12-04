package ku.network.malang.model

data class LobbyItem(
    val roomId: Int,
    val roomName: String,
    val quizCount: Int,
    val currentPlayers: Int,
    val maxPlayers: Int,
)
