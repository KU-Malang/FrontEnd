package ku.network.malang.dto.response

import org.json.JSONObject

data class SelectTopicRepDataDto(val topic: String) {
    companion object {
        fun fromJson(data: JSONObject): SelectTopicRepDataDto {
            return SelectTopicRepDataDto(
                topic = data.getString("topic")
            )
        }
    }
}