package ku.network.malang.dto.response

import ku.network.malang.network.Response

data class EnterRoomRepDto(
    val messageType: Int,
    val status: String,
    val message: String
) : Response {
    companion object {
        fun fromJson(jsonString: String): EnterRoomRepDto {
            return Response.fromJson(jsonString) { jsonObject ->
                EnterRoomRepDto(
                    messageType = jsonObject.getInt("messageType"),
                    status = jsonObject.getString("status"),
                    message = jsonObject.getString("message")
                )
            }
        }
    }
}