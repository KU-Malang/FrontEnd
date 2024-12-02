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
        fun fromJson(jsonString: String): LoginRepDto {
            val jsonObject = JSONObject(jsonString)
            val data = jsonObject.optJSONObject("data")
            return LoginRepDto(
                messageType = jsonObject.getInt("messageType"),
                status = jsonObject.getString("status"),
                message = jsonObject.getString("message"),
                userId = data?.optInt("userId")
            )
        }
    }
}