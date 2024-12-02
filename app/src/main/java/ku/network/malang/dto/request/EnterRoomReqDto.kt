package ku.network.malang.dto.request

import ku.network.malang.network.Request
import org.json.JSONObject

data class EnterRoomReqDto(
    val messageType: Int = 4, // 메시지 타입 고정
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