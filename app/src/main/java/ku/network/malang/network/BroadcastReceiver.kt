package ku.network.malang.network

import android.util.Log
import ku.network.malang.dto.response.BaseResponse
import ku.network.malang.dto.response.CorrectRepDataDto
import ku.network.malang.dto.response.EnterRoomRepDataDto
import ku.network.malang.dto.response.GameResultRepDataDto
import ku.network.malang.dto.response.IncorrectRepDataDto
import ku.network.malang.dto.response.LeaveRoomRepDataDto
import ku.network.malang.dto.response.NewQuizRepDataDto
import ku.network.malang.dto.response.SelectTopicRepDataDto
import org.json.JSONObject

class BroadcastReceiver {

    interface BroadcastListener {
        fun enterRoomResponse(data: EnterRoomRepDataDto?)
        fun selectTopicResponse(data: SelectTopicRepDataDto?)
        fun newQuizResponse(data: NewQuizRepDataDto?)
        fun correctResponse(data: CorrectRepDataDto?)
        fun incorrectResponse(data: IncorrectRepDataDto?)
        fun gameResultResponse(data: GameResultRepDataDto?)
        fun leaveRoomResponse(data: LeaveRoomRepDataDto?)
        fun gameStartResponse(status: String)
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
                Log.d("응답 수신", "브로드캐스트 받음: ${res.message}")
                when(res.messageType) {
                    MessageType.ENTER_ROOM.idx -> {
                        if(res.data == null) broadcastListener?.enterRoomResponse(null)
                        else broadcastListener?.enterRoomResponse(EnterRoomRepDataDto.fromJson(res.data))
                    }
                    MessageType.SELECT_QUIZ_TOPIC.idx -> {
                        if(res.data == null) broadcastListener?.selectTopicResponse(null)
                        else broadcastListener?.selectTopicResponse(SelectTopicRepDataDto.fromJson(res.data))
                    }
                    MessageType.NEW_QUIZ.idx -> {
                        if(res.data == null) broadcastListener?.newQuizResponse(null)
                        else broadcastListener?.newQuizResponse(NewQuizRepDataDto.fromJson(res.data))
                    }
                    MessageType.CORRECT.idx -> {
                        if(res.data == null) broadcastListener?.correctResponse(null)
                        else broadcastListener?.correctResponse(CorrectRepDataDto.fromJson(res.data))
                    }
                    MessageType.INCORRECT.idx -> {
                        if(res.data == null) broadcastListener?.incorrectResponse(null)
                        else broadcastListener?.incorrectResponse(IncorrectRepDataDto.fromJson(res.data))
                    }
                    MessageType.GAME_RESULT.idx -> {
                        if(res.data == null) broadcastListener?.gameResultResponse(null)
                        else broadcastListener?.gameResultResponse(GameResultRepDataDto.fromJson(res.data))
                    }
                    MessageType.LEAVE_ROOM.idx -> {
                        if(res.data == null) broadcastListener?.leaveRoomResponse(null)
                        else broadcastListener?.leaveRoomResponse(LeaveRoomRepDataDto.fromJson(res.data))
                    }
                    MessageType.GAME_START.idx -> {
                        broadcastListener?.gameStartResponse(res.status)
                    }
                }
            },
            {
                broadcastListener?.disconnect()
            }
        )
    }

    fun stopListening() {
        SocketClient.stopListening()
    }

    fun setBroadcastListener(listener: BroadcastListener) {
        broadcastListener = listener
    }

}