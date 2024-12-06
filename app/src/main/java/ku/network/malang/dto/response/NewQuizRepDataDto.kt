package ku.network.malang.dto.response

import org.json.JSONObject

data class NewQuizRepDataDto(
    val topic: String,
    val quizNumber: Int,
    val userStatus: Boolean
) {
    companion object {
        fun fromJson(data: JSONObject): NewQuizRepDataDto {
            return NewQuizRepDataDto(
                topic = data.getString("topic"),
                quizNumber = data.getInt("quizNumber"),
                userStatus = data.getBoolean("userStatus")
            )
        }
    }
}
