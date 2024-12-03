package ku.network.malang.dto.request

import ku.network.malang.network.Request
import org.json.JSONObject

data class LobbyReqDto(
    val messageType: Int = 3,
    val userId: Int
):Request {
    override fun toJson(): String {
        return JSONObject().apply {
            put("messageType", messageType)
            put("userId", userId)
        }.toString()
    }
}