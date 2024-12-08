package ku.network.malang.dto.request

import ku.network.malang.network.Request
import org.json.JSONObject

data class SelectTopicReqDto(
    val messageType: Int = 5,
    val roomId: Int,
    val topic: String
) : Request {
    override fun toJson(): String {
        return JSONObject().apply {
            put("messageType", messageType)
            put("topic", topic)
            put("roomId", roomId)
        }.toString()
    }
}

