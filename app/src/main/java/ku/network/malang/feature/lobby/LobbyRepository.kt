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
            toJson = { JSONObject(mapOf("messageType" to it.messageType, "userId" to it.userId)).toString() },
            fromJson = { responseString ->
                val jsonObject = JSONObject(responseString)
                val dataObject = jsonObject.optJSONObject("data")
                LobbyRepDto(
                    messageType = jsonObject.getInt("messageType"),
                    status = jsonObject.getString("status"),
                    message = jsonObject.getString("message"),
                    data = dataObject?.let { data ->
                        LobbyRepDto.LobbyData(
                            nickname = data.getString("nickname"),
                            rating = data.getInt("rating"),
                            rooms = data.getJSONArray("rooms").let { roomsArray ->
                                (0 until roomsArray.length()).map { index ->
                                    val room = roomsArray.getJSONObject(index)
                                    LobbyRepDto.LobbyRoom(
                                        roomId = room.getInt("roomId"),
                                        roomName = room.getString("roomName"),
                                        quizCount = room.getInt("quizCount"),
                                        currentPlayers = room.getInt("currentPlayers"),
                                        maxPlayers = room.getInt("maxPlayers")
                                    )
                                }
                            }
                        )
                    }
                )
            }
        )
    }
}
