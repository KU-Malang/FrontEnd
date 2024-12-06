package ku.network.malang.dto.response

data class LeaveRoomRepDataDto(
    val roomName: String,
    val hostUser: String,
    val userList: List<User>
) {
    data class User(val userName: String)
}