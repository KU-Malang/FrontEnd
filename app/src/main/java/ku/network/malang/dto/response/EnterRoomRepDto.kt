package ku.network.malang.dto.response

import android.util.Log
import ku.network.malang.network.Response

data class EnterRoomRepDto(
    val messageType: Int,
    val status: String,
    val message: String,
    val data: EnterRoomRepDataDto
) : Response {
    companion object {
        fun fromJson(jsonString: String): EnterRoomRepDto {
            Log.d("응답 수신", "fromJson: $jsonString")
            return Response.fromJson(jsonString) { jsonObject ->
                val status = jsonObject.getString("status")
                val data = if(status != "success") EnterRoomRepDataDto("", ArrayList()) else EnterRoomRepDataDto.fromJson(jsonObject.getJSONObject("data"))
                EnterRoomRepDto(
                    messageType = jsonObject.getInt("messageType"),
                    status = status,
                    message = jsonObject.getString("message"),
                    data = data
                )
            }
        }
    }
}