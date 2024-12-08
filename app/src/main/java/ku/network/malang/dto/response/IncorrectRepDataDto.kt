package ku.network.malang.dto.response

import org.json.JSONObject

data class IncorrectRepDataDto(
    val userNickname: String,
    val wrongAnswer: String
) {
    companion object {
        fun fromJson(data: JSONObject): IncorrectRepDataDto {
            return IncorrectRepDataDto(
                userNickname = data.getString("userNickname"),
                wrongAnswer = data.getString("wrongAnswer")
            )
        }
    }
}
