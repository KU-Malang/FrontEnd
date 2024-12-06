package ku.network.malang.dto.response

data class EnterRoomRepDataDto(
    val roomName: String,
    val userList: List<User>
) {
    data class User(val userName: String)
}
