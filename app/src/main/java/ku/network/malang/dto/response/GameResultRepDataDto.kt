package ku.network.malang.dto.response

import org.json.JSONObject

data class GameResultRepDataDto(
    val users: List<User>
) {
    data class User(
        val userId: Int,
        val nickname: String,
        val change: String,
        val rating: Int
    )
    companion object {
        fun fromJson(data: JSONObject): GameResultRepDataDto {
            return GameResultRepDataDto(
                users = data.getJSONArray("users").let { arr ->
                    (0 until arr.length()).map { idx ->
                        val user = arr.getJSONObject(idx)
                        User(
                            userId = user.getInt("userId"),
                            nickname = user.getString("nickname"),
                            change = user.getString("change"),
                            rating = user.getInt("rating")
                        )
                    }
                }
            )
        }
    }
}
