package ku.network.malang.network

import org.json.JSONObject

interface Response {
    companion object {
        fun <T : Response> fromJson(jsonString: String, creator: (JSONObject) -> T): T {
            return creator(JSONObject(jsonString))
        }
    }
}