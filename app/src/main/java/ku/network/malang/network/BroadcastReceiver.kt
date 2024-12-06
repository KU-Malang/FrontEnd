package ku.network.malang.network

import ku.network.malang.dto.response.BaseResponse
import org.json.JSONObject

class BroadcastReceiver {

    interface BroadcastListener {
        fun enterRoomResponse()
        fun selectQuizTopicResponse()
        fun newQuizResponse()
        fun correctResponse()
        fun incorrectResponse()
        fun gameResultResponse()
        fun leaveRoomResponse()
        fun gameStartResponse()
        fun disconnect()
    }

    enum class MessageType(val idx: Int) {
        ENTER_ROOM(4),
        SELECT_QUIZ_TOPIC(5),
        NEW_QUIZ(6),
        CORRECT(7),
        INCORRECT(8),
        GAME_RESULT(9),
        LEAVE_ROOM(10),
        GAME_START(11)
    }

    private var broadcastListener: BroadcastListener? = null

    fun startListening() {
        SocketClient.startListening(
            {
                val jsonObject = JSONObject(it)
                val res = BaseResponse(
                    messageType = jsonObject.getInt("messageType"),
                    status = jsonObject.getString("status"),
                    message = jsonObject.getString("message"),
                    data = jsonObject.optJSONObject("data")
                )

                broadcastListener?.gameStartResponse()
            },
            {
                broadcastListener?.disconnect()
            }
        )
    }

}