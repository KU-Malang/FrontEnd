package ku.network.malang.feature.lobby

import ku.network.malang.dto.request.LobbyReqDto
import ku.network.malang.dto.response.LobbyRepDto
import ku.network.malang.network.SocketClient
import org.json.JSONObject

class LobbyRepository {
    fun fetchLobbyData(userId: Int): LobbyRepDto? {
        val requestDto = LobbyReqDto(userId = userId)
        return SocketClient.sendRequest(
            request = requestDto,
            toJson = { it.toJson() },
            fromJson = { responseString -> LobbyRepDto.fromJson(responseString) }
        )
    }
}
