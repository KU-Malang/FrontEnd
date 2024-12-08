package ku.network.malang.dto.request

import ku.network.malang.network.Request
import org.json.JSONObject

data class NewQuizReqDto(
    val messageType: Int = 6,
    val roomId: Int,
    val quizType: String
) : Request {
    override fun toJson(): String {
        return JSONObject().apply {
            put("messageType", messageType)
            put("roomId", roomId)
            put("quizType", quizType)
        }.toString()
    }
}
