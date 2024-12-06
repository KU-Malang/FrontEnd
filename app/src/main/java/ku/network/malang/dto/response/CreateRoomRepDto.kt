package ku.network.malang.dto.response

import android.util.Log
import ku.network.malang.network.Response

data class CreateRoomRepDto(
    val messageType: Int,
    val status: String,
    val message: String,
    val roomId: Int
) : Response {
    companion object {
        fun fromJson(jsonString: String): CreateRoomRepDto {
            Log.d("응답 방 생성", "fromJson: $jsonString")
            return Response.fromJson(jsonString) { jsonObject ->
                val data = jsonObject.optJSONObject("data")
                CreateRoomRepDto(
                    messageType = jsonObject.getInt("messageType"),
                    status = jsonObject.getString("status"),
                    message = jsonObject.getString("message"),
                    roomId = data?.optInt("roomId") ?: 0
                )
            }
        }
    }
}