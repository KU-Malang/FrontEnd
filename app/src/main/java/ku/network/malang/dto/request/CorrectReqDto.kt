package ku.network.malang.dto.request

import ku.network.malang.network.Request
import org.json.JSONObject

data class CorrectReqDto(
    val messageType: Int = 7,
    val roomId: Int,
    val userId: Int,
    val currentQuizIndex: Int,
    val correctAnswer: String
) : Request {
    override fun toJson(): String {
        return JSONObject().apply {
            put("messageType", messageType)
            put("roomId", roomId)
            put("userId", userId)
            put("currentQuizIndex", currentQuizIndex)
            put("correctAnswer", correctAnswer)
        }.toString()
    }
}

