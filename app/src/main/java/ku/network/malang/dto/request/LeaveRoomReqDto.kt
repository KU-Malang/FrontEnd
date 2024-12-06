package ku.network.malang.dto.request

import ku.network.malang.network.Request
import org.json.JSONObject

data class LeaveRoomReqDto(
    val messageType: Int = 10,
    val userId: Int,
    val roomId: Int
) : Request {
    override fun toJson(): String {
        return JSONObject().apply {
            put("messageType", messageType)
            put("userId", userId)
            put("roomId", roomId)
        }.toString()
    }
}
