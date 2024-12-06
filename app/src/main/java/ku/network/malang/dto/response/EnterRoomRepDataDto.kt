package ku.network.malang.dto.response

import ku.network.malang.network.Response
import org.json.JSONObject

data class EnterRoomRepDataDto (
    val roomName: String,
    val userList: List<User>
) {
    data class User(val userName: String)
    companion object {
        fun fromJson(data: JSONObject): EnterRoomRepDataDto {
            return EnterRoomRepDataDto(
                roomName = data.getString("roomName"),
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
