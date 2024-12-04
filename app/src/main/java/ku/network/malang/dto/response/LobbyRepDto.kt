package ku.network.malang.dto.response

import android.util.Log
import ku.network.malang.model.LobbyItem
import ku.network.malang.network.Response

data class LobbyRepDto(
    val messageType: Int,
    val status: String,
    val message: String,
    val data: LobbyData?
):Response {
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

    companion object {
        fun fromJson(jsonString: String): LobbyRepDto {
            Log.d("로비 방 목록 조회", "Parsing JSON: $jsonString") // 전체 JSON 로그
            return Response.fromJson(jsonString) { jsonObject ->
                val dataObject = jsonObject.optJSONObject("data")
                LobbyRepDto(
                    messageType = jsonObject.getInt("messageType"),
                    status = jsonObject.getString("status"),
                    message = jsonObject.getString("message"),
                    data = dataObject?.let { data ->
                        LobbyData(
                            nickname = data.getString("nickname"),
                            rating = data.getInt("rating"),
                            rooms = data.getJSONArray("rooms").let { roomsArray ->
                                (0 until roomsArray.length()).map { index ->
                                    val room = roomsArray.getJSONObject(index)
                                    LobbyRoom(
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
        }
    }

    // 확장 함수: LobbyRepDto -> List<LobbyItem>
    fun toLobbyItems(): List<LobbyItem> {
        return data?.rooms?.map { room ->
            LobbyItem(
                roomId = room.roomId,
                roomName = room.roomName,
                quizCount = room.quizCount,
                currentPlayers = room.currentPlayers,
                maxPlayers = room.maxPlayers
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