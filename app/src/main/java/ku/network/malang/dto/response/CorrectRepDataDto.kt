package ku.network.malang.dto.response

import org.json.JSONObject

data class CorrectRepDataDto(
    val userNickname: String,
    val correctAnswer: String
) {
    companion object {
        fun fromJson(data: JSONObject): CorrectRepDataDto {
            return CorrectRepDataDto(
                userNickname = data.getString("userNickname"),
                correctAnswer = data.getString("correctAnswer")
            )
        }
    }
}
