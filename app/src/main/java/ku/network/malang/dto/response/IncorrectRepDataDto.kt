package ku.network.malang.dto.response

import org.json.JSONObject

data class IncorrectRepDataDto(
    val userId: Int,
    val wrongAnswer: String
) {
    companion object {
        fun fromJson(data: JSONObject): IncorrectRepDataDto {
            return IncorrectRepDataDto(
                userId = data.getInt("userId"),
                wrongAnswer = data.getString("wrongAnswer")
            )
        }
    }
}
