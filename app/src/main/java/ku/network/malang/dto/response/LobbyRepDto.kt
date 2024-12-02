package ku.network.malang.dto.response

import ku.network.malang.model.LobbyItem

data class LobbyRepDto(
    val messageType: Int,
    val status: String,
    val message: String,
    val data: LobbyData?
) {
    data class LobbyData(
        val nickname: String,
        val rating: Int,
        val rooms: List<LobbyRoom>
    )

    data class LobbyRoom(
        val roomId: Int,
        val roomName: String,
        val quizCount: Int,
        val currentPlayers: Int,
        val maxPlayers: Int
    )

    // 확장 함수: LobbyRepDto -> List<LobbyItem>
    fun toLobbyItems(): List<LobbyItem> {
        return data?.rooms?.map { room ->
            LobbyItem(
                title = room.roomName,
                totalQuestions = "${room.quizCount}문제",
                progress = "${room.currentPlayers}/${room.maxPlayers}"
            )
        } ?: emptyList()
    }

    // 확장 함수: LobbyRepDto -> Nickname
    fun getNickname(): String {
        return data?.nickname ?: "알 수 없음"
    }

    // 확장 함수: LobbyRepDto -> Rating
    fun getRating(): Int {
        return data?.rating ?: 0
    }
}