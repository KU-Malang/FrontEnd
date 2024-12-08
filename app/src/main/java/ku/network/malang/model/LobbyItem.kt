package ku.network.malang.model

import ku.network.malang.dto.response.EnterRoomRepDataDto

data class LobbyItem(
    val roomId: Int,
    val roomName: String,
    val quizCount: Int,
    val currentPlayers: Int,
    val maxPlayers: Int,
    val playerList: ArrayList<EnterRoomRepDataDto.User>,
    val isHost: Boolean
)
