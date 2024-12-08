package ku.network.malang.feature.lobby

import ku.network.malang.dto.response.EnterRoomRepDataDto

class EnterRoomInteractor(private val repository: EnterRoomRepository) {
    fun enterRoom(userId: Int, roomId: Int): Result<EnterRoomRepDataDto> {
        val response = repository.enterRoom(userId, roomId)

        return if (response != null) {
            when (response.status) {
                "success" -> Result.success(response.data) // 성공 메시지
                else -> Result.failure(Exception("방 입장 실패: ${response.message}"))
            }
        } else {
            Result.failure(Exception("서버와의 연결에 실패했습니다."))
        }
    }
}