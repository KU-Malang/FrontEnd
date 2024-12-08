package ku.network.malang.dto.request

import ku.network.malang.network.Request
import org.json.JSONObject

data class GameStartReqDto(
    val messageType: Int = 11,
    val userId: Int,
    val roomId: Int
) : Request {
    override fun toJson(): String {
        return JSONObject().apply {
            put("messageType", messageType)
            put("userId", userId)
            put("roomId", roomId)
        }.toString()
    }
}
