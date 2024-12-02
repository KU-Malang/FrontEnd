package ku.network.malang.feature.lobby

class CreateRoomInteractor(private val repository: CreateRoomRepository) {
    fun createRoom(
        roomName: String,
        maxPlayers: Int,
        hostUserId: Int,
        quizCount: Int
    ): Result<String> {
        val response = repository.createRoom(roomName, maxPlayers, hostUserId, quizCount)

        return if (response != null) {
            when (response.status) {
                "success" -> Result.success("방 생성 성공! Room ID: ${response.roomId}")
                "error" -> Result.failure(Exception("방 생성 실패: ${response.message}"))
                else -> Result.failure(Exception("알 수 없는 오류: ${response.message}"))
            }
        } else {
            Result.failure(Exception("서버와의 연결에 실패했습니다."))
        }
    }
}