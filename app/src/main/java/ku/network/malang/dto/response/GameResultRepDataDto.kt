package ku.network.malang.dto.response

data class GameResultRepDataDto(
    val users: List<User>
) {
    data class User(
        val userId: Int,
        val nickname: String,
        val change: String,
        val rating: Int
    )
}
