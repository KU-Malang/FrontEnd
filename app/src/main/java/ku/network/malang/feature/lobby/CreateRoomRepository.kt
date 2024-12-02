package ku.network.malang.feature.lobby

import ku.network.malang.dto.request.CreateRoomReqDto
import ku.network.malang.dto.response.CreateRoomRepDto
import ku.network.malang.network.SocketClient

class CreateRoomRepository {
    fun createRoom(
        roomName: String,
        maxPlayers: Int,
        hostUserId: Int,
        quizCount: Int
    ): CreateRoomRepDto? {
        val requestDto = CreateRoomReqDto(
            roomName = roomName,
            maxPlayers = maxPlayers,
            hostUserId = hostUserId,
            quizCount = quizCount
        )

        return SocketClient.sendRequest(
            request = requestDto,
            toJson = { it.toJson() },
            fromJson = { CreateRoomRepDto.fromJson(it) }
        )
    }
}