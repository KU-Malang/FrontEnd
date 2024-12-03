package ku.network.malang.dto.request

import ku.network.malang.network.Request
import org.json.JSONObject

data class LoginReqDto(
    val messageType: Int,
    val nickname: String,
    val password: String
) : Request {
    override fun toJson(): String {
        return JSONObject().apply {
            put("messageType", messageType)
            put("nickname", nickname)
            put("password", password)
        }.toString()
    }
}