package ku.network.malang.dto.request

import ku.network.malang.network.Request
import org.json.JSONObject

data class CreateRoomReqDto(
    val messageType: Int = 1,
    val roomName: String,
    val maxPlayers: Int,
    val hostUserId: Int,
    val quizCount: Int
) : Request {
    override fun toJson(): String {
        return JSONObject().apply {
            put("messageType", messageType)
            put("roomName", roomName)
            put("maxPlayers", maxPlayers)
            put("hostUserId", hostUserId)
            put("quizCount", quizCount)
        }.toString()
    }
}