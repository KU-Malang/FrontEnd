package ku.network.malang.dto.response

import ku.network.malang.network.Response
import org.json.JSONObject

data class LoginRepDto(
    val messageType: Int,
    val status: String,
    val message: String,
    val userId: Int? = null
): Response{
    companion object {
        // JSON 문자열 -> LoginRepDto 변환 로직
        fun fromJson(jsonString: String): LoginRepDto {
            return Response.fromJson(jsonString) { jsonObject ->
                val data = jsonObject.optJSONObject("data")
                LoginRepDto(
                    messageType = jsonObject.getInt("messageType"),
                    status = jsonObject.getString("status"),
                    message = jsonObject.getString("message"),
                    userId = data?.optInt("userId")
                )
            }
        }
    }
}