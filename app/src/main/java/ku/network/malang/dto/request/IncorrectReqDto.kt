package ku.network.malang.dto.request

import ku.network.malang.network.Request
import org.json.JSONObject

data class IncorrectReqDto(
    val messageType: Int = 8,
    val roomId: Int,
    val userId: Int,
    val wrongAnswer: String
) : Request {
    override fun toJson(): String {
        return JSONObject().apply {
            put("messageType", messageType)
            put("roomId", roomId)
            put("userId", userId)
            put("wrongAnswer", wrongAnswer)
        }.toString()
    }
}