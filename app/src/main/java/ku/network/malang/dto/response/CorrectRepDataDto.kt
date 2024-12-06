package ku.network.malang.dto.response

import org.json.JSONObject

data class CorrectRepDataDto(
    val userId: Int,
    val correctAnswer: String
) {
    companion object {
        fun fromJson(data: JSONObject): CorrectRepDataDto {
            return CorrectRepDataDto(
                userId = data.getInt("userId"),
                correctAnswer = data.getString("correctAnswer")
            )
        }
    }
}
