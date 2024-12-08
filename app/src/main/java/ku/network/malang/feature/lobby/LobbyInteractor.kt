package ku.network.malang.feature.lobby

import ku.network.malang.MalangApplication.Companion.userNickname
import ku.network.malang.model.LobbyItem

class LobbyInteractor(private val repository: LobbyRepository) {
    fun fetchLobbyData(userId: Int): Result<Triple<List<LobbyItem>, String, Int>> {
        val response = repository.fetchLobbyData(userId)

        return if (response != null) {
            when (response.status) {
                "success" -> {
                    val nickname = response.getNickname() // 확장 함수 사용
                    userNickname = nickname
                    val rating = response.getRating()     // 확장 함수 사용
                    val lobbyItems = response.toLobbyItems()
                    Result.success(Triple(lobbyItems, nickname, rating))
                }
                "3001" -> Result.failure(Exception("존재하지 않는 유저 ID입니다."))
                else -> Result.failure(Exception(response.message))
            }
        } else {
            Result.failure(Exception("서버와의 연결에 실패했습니다."))
        }
    }
}
