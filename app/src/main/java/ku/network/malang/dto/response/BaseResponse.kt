package ku.network.malang.dto.response

import ku.network.malang.network.Response
import org.json.JSONObject

data class BaseResponse(
    val messageType: Int,
    val status: String,
    val message: String,
    val data: JSONObject?
) : Response {

    companion object {
        fun fromJson(jsonString: String): BaseResponse {
            return Response.fromJson(jsonString) { jsonObject ->
                BaseResponse(
                    messageType = jsonObject.getInt("messageType"),
                    status = jsonObject.getString("status"),
                    message = jsonObject.getString("message"),
                    data = jsonObject.optJSONObject("data")
                )
            }
        }
    }

}