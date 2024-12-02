package ku.network.malang.feature.lobby

import ku.network.malang.dto.request.EnterRoomReqDto
import ku.network.malang.dto.response.EnterRoomRepDto
import ku.network.malang.network.SocketClient

class EnterRoomRepository {
    fun enterRoom(userId: Int, roomId: Int): EnterRoomRepDto? {
        val requestDto = EnterRoomReqDto(userId = userId, roomId = roomId)

        return SocketClient.sendRequest(
            request = requestDto,
            toJson = { it.toJson() },
            fromJson = { EnterRoomRepDto.fromJson(it) }
        )
    }
}