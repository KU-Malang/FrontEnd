package ku.network.malang.dto.response

import org.json.JSONObject

data class LeaveRoomRepDataDto(
    val roomName: String,
    val hostUser: String,
    val userList: List<User>
) {
    data class User(val userName: String)
    companion object {
        fun fromJson(data: JSONObject): LeaveRoomRepDataDto {
            return LeaveRoomRepDataDto(
                roomName = data.getString("roomName"),
                hostUser = data.getString("hostUser"),
                userList = data.getJSONArray("userList").let { arr ->
                    (0 until arr.length()).map { idx ->
                        val userName = arr.getJSONObject(idx)
                        User(
                            userName = userName.getString("userName")
                        )
                    }
                }
            )
        }
    }
}