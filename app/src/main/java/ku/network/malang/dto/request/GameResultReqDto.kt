package ku.network.malang.dto.request

import ku.network.malang.network.Request
import org.json.JSONObject

data class GameResultReqDto(
    val messageType: Int = 9,
    val roomId: Int
) : Request {
    override fun toJson(): String {
        return JSONObject().apply {
            put("messageType", messageType)
            put("roomId", roomId)
        }.toString()
    }
}