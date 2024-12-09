package ku.network.malang.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ku.network.malang.MalangApplication.Companion.getRoomInfo
import ku.network.malang.MalangApplication.Companion.getUserId
import ku.network.malang.MalangApplication.Companion.quizs
import ku.network.malang.MalangApplication.Companion.userNickname
import ku.network.malang.R
import ku.network.malang.databinding.ActivityGameBinding
import ku.network.malang.dialog.ExitDialog
import ku.network.malang.dialog.GameOverDialog
import ku.network.malang.dialog.InformationDialog
import ku.network.malang.dialog.SelectTopicDialog
import ku.network.malang.dialog.TimerDialog
import ku.network.malang.dto.request.CorrectReqDto
import ku.network.malang.dto.request.GameResultReqDto
import ku.network.malang.dto.request.GameStartReqDto
import ku.network.malang.dto.request.IncorrectReqDto
import ku.network.malang.dto.request.LeaveRoomReqDto
import ku.network.malang.dto.request.NewQuizReqDto
import ku.network.malang.dto.request.SelectTopicReqDto
import ku.network.malang.dto.response.CorrectRepDataDto
import ku.network.malang.dto.response.EnterRoomRepDataDto
import ku.network.malang.dto.response.GameResultRepDataDto
import ku.network.malang.dto.response.IncorrectRepDataDto
import ku.network.malang.dto.response.LeaveRoomRepDataDto
import ku.network.malang.dto.response.NewQuizRepDataDto
import ku.network.malang.dto.response.SelectTopicRepDataDto
import ku.network.malang.feature.game.PlayerAdapter
import ku.network.malang.feature.game.WrongAnswerAdapter
import ku.network.malang.model.Quiz
import ku.network.malang.model.QuizType
import ku.network.malang.network.BroadcastReceiver
import ku.network.malang.network.SocketClient

class GameActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGameBinding
    private val broadcastReceiver = BroadcastReceiver()
    private var countdownJob: Job? = null
    private var isGameStart = false
    private var quizType = QuizType.POPULAR_CULTURE
    private var currentQuiz : Quiz? = null
    private var currentQuizIndex = -1
    private var currentQuizType = QuizType.PRACTICE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.headerGameTitle.text = getRoomInfo()!!.roomName

        // RecyclerView 설정
        binding.gamePlayerList.layoutManager = GridLayoutManager(this, 4)
        binding.gamePlayerList.adapter = getRoomInfo()?.let { PlayerAdapter(it.playerList) }

        binding.gameWrongAnswerList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.gameWrongAnswerList.adapter = WrongAnswerAdapter()

        initBroadcastReceiver()

        if(!getRoomInfo()!!.isHost) {
            binding.gameStartButton.text = "대기중"
            binding.gameStartButton.isEnabled = false
        }
        binding.gameStartButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                SocketClient.sendRequestOnly(
                    request = GameStartReqDto(
                        userId = getUserId()!!,
                        roomId = getRoomInfo()!!.roomId
                    ),
                    toJson = { it.toJson() }
                )
            }
        }
        binding.headerGameOut.setOnClickListener {
            leaveRoom()
        }
        binding.footerSendButton.setOnClickListener {
            sendAnswer()
        }
    }

    private fun initBroadcastReceiver() {
        broadcastReceiver.setBroadcastListener(object : BroadcastReceiver.BroadcastListener {
            override fun enterRoomResponse(data: EnterRoomRepDataDto?) {
                runOnUiThread {
                    if (data == null) Toast.makeText(this@GameActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    else (binding.gamePlayerList.adapter as PlayerAdapter).updatePlayers(data.userList)
                }
            }

            override fun selectTopicResponse(data: SelectTopicRepDataDto?) {
                runOnUiThread {
                    if (data == null) Toast.makeText(this@GameActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    else {
                        quizType = QuizType.getQuizType(data.topic)!!
                        val dialog = InformationDialog(this@GameActivity, InformationDialog.Type.GAME_START, QuizType.getQuizType(data.topic)?.koreanName)
                        dialog.show()
                        if(getRoomInfo()!!.isHost) {
                            CoroutineScope(Dispatchers.IO).launch {
                                SocketClient.sendRequestOnly(
                                    request = NewQuizReqDto(
                                        roomId = getRoomInfo()!!.roomId,
                                        quizType = data.topic
                                    ),
                                    toJson = { it.toJson() }
                                )
                            }
                        }
                    }
                }
            }

            override fun newQuizResponse(data: NewQuizRepDataDto?) {
                runOnUiThread {
                    if (data == null) Toast.makeText(this@GameActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    else newQuiz(data)
                }
            }

            override fun correctResponse(data: CorrectRepDataDto?) {
                runOnUiThread {
                    if (data == null) Toast.makeText(this@GameActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    else afterCorrect(data)
                }
            }

            override fun incorrectResponse(data: IncorrectRepDataDto?) {
                runOnUiThread {
                    if (data == null) Toast.makeText(this@GameActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    else {
                        (binding.gameWrongAnswerList.adapter as WrongAnswerAdapter).addMessage(data.userNickname, data.wrongAnswer)
                    }
                }
            }

            override fun gameResultResponse(data: GameResultRepDataDto?) {
                runOnUiThread {
                    if (data == null) Toast.makeText(this@GameActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    else {
                        val dialog = GameOverDialog(this@GameActivity, data.users)
                        dialog.setOnExitListener(object : GameOverDialog.OnExitListener {
                            override fun exit() {
                                broadcastReceiver.stopListening()
                                this@GameActivity.finish()
                            }
                        })
                        dialog.show()
                    }
                }
            }

            override fun leaveRoomResponse(data: LeaveRoomRepDataDto?) {
                runOnUiThread {
                    if (data == null) Toast.makeText(this@GameActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    else (binding.gamePlayerList.adapter as PlayerAdapter).updatePlayers(data.userList)
                }
            }

            override fun gameStartResponse(status: String) {
                runOnUiThread {
                    if(status == "success") gameStart()
                    else Toast.makeText(this@GameActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
                }
            }

            override fun disconnect() {
                CoroutineScope(Dispatchers.IO).launch {
                    SocketClient.sendRequestOnly(
                        request = LeaveRoomReqDto(
                            userId = getUserId()!!,
                            roomId = getRoomInfo()!!.roomId
                        ),
                        toJson = { it.toJson() }
                    )
                }
                runOnUiThread {
                    Toast.makeText(this@GameActivity, "서버와 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show()
                    this@GameActivity.finish()
                }
            }
        })
        broadcastReceiver.startListening()
    }

    private fun afterCorrect(data: CorrectRepDataDto) {
        stopTimer()
        (binding.gamePlayerList.adapter as PlayerAdapter).players.forEach {
            if(it.userName == data.userNickname) {
                it.score++
            }
        }

        val dialog = TimerDialog(this, TimerDialog.Type.ANSWER, "정답은 ${data.correctAnswer}입니다")
        dialog.setOnTimeOutListener(object : TimerDialog.OnTimeOutListener {
            override fun timeOut() {
                Log.d("afterCorrect", "$currentQuizIndex, ${getRoomInfo()?.quizCount}")
                if(currentQuizType == QuizType.PRACTICE) {
                    if(userNickname == data.userNickname) selectTopic()
                    return
                }
                if(currentQuizIndex == getRoomInfo()?.quizCount) {
                    if(getRoomInfo()!!.isHost)
                        gameOver()
                    return
                }
                if(currentQuizIndex == getRoomInfo()!!.quizCount / 2) {
                    redemption()
                    return
                }
                if(currentQuizType == QuizType.REDEMPTION) {
                    (binding.gamePlayerList.adapter as PlayerAdapter).players.forEach {
                        if(it.userName == data.userNickname) {
                            it.isActivated = true
                        }
                    }
                    binding.gamePlayerList.adapter?.notifyDataSetChanged()
                }
                if(getRoomInfo()!!.isHost) {
                    CoroutineScope(Dispatchers.IO).launch {
                        SocketClient.sendRequestOnly(
                            request = NewQuizReqDto(
                                roomId = getRoomInfo()!!.roomId,
                                quizType = quizType.toString()
                            ),
                            toJson = { it.toJson() }
                        )
                    }
                }
            }
        })
        dialog.show()
    }

    private fun redemption() {
        (binding.gamePlayerList.adapter as PlayerAdapter).let { adapter ->
            val players = adapter.players
            val sortedPlayers = players.sortedBy { it.score }
            val halfIndex = players.size / 2

            sortedPlayers.take(halfIndex).forEach { player ->
                player.isActivated = false
            }

            adapter.notifyDataSetChanged()
        }

        val dialog = InformationDialog(this, InformationDialog.Type.CONSOLATION_MATCH_START, null)
        dialog.show()
        if(getRoomInfo()!!.isHost) {
            CoroutineScope(Dispatchers.IO).launch {
                SocketClient.sendRequestOnly(
                    request = NewQuizReqDto(
                        roomId = getRoomInfo()!!.roomId,
                        quizType = QuizType.REDEMPTION.toString()
                    ),
                    toJson = { it.toJson() }
                )
            }
        }
    }

    private fun gameOver() {
        CoroutineScope(Dispatchers.IO).launch {
            SocketClient.sendRequestOnly(
                request = GameResultReqDto(
                    roomId = getRoomInfo()!!.roomId,
                ),
                toJson = { it.toJson() }
            )
        }
    }

    private fun gameStart() {
        isGameStart = true
        Toast.makeText(this@GameActivity, "게임 시작!!", Toast.LENGTH_SHORT).show()

        binding.gameStartButton.visibility = View.INVISIBLE

        val dialog = InformationDialog(this, InformationDialog.Type.PRACTICE_QUIZ, null)
        dialog.show()

        if(getRoomInfo()!!.isHost) {
            CoroutineScope(Dispatchers.IO).launch {
                SocketClient.sendRequestOnly(
                    request = NewQuizReqDto(
                        roomId = getRoomInfo()!!.roomId,
                        quizType = QuizType.PRACTICE.toString()
                    ),
                    toJson = { it.toJson() }
                )
            }
        }
    }

    private fun startTimer(startTime: Int, onTimeDecline: (time: Int) -> Unit) {
        countdownJob?.cancel()

        countdownJob = CoroutineScope(Dispatchers.Main).launch {
            var time = startTime
            while (time >= 0) {
                binding.headerGameRemainingTime.text = time.toString()
                delay(1000)
                time--
                onTimeDecline(time)
            }
        }
    }

    private fun stopTimer() {
        countdownJob?.cancel()
    }

    private fun sendAnswer() {
        val answer = binding.footerMessageInput.text.toString()
        binding.footerMessageInput.text.clear()

        if(currentQuiz?.answer == answer) {
            CoroutineScope(Dispatchers.IO).launch {
                SocketClient.sendRequestOnly(
                    request = CorrectReqDto(
                        roomId = getRoomInfo()!!.roomId,
                        userId = getUserId()!!,
                        currentQuizIndex = currentQuizIndex,
                        correctAnswer = answer
                    ),
                    toJson = { it.toJson() }
                )
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                SocketClient.sendRequestOnly(
                    request = IncorrectReqDto(
                        roomId = getRoomInfo()!!.roomId,
                        userId = getUserId()!!,
                        wrongAnswer = answer
                    ),
                    toJson = { it.toJson() }
                )
            }
        }
    }

    private fun selectTopic() {
        val dialog = SelectTopicDialog(this)
        dialog.setOnSelectListener(object: SelectTopicDialog.OnSelectListener {
            override fun select(id: Int) {
                var topic = ""
                when(id) {
                    R.id.radio_common -> topic = QuizType.GENERAL_KNOWLEDGE.toString()
                    R.id.radio_computer -> topic = QuizType.COMPUTER.toString()
                    R.id.radio_culture -> topic = QuizType.POPULAR_CULTURE.toString()
                    R.id.radio_geography -> topic = QuizType.GEOGRAPHY.toString()
                    R.id.radio_history -> topic = QuizType.HISTORY.toString()
                    R.id.radio_current_event -> topic = QuizType.CURRENT_AFFAIRS.toString()
                }
                quizType = QuizType.getQuizType(topic)!!
                CoroutineScope(Dispatchers.IO).launch {
                    SocketClient.sendRequestOnly(
                        request = SelectTopicReqDto(
                            roomId = getRoomInfo()!!.roomId,
                            topic = topic
                        ),
                        toJson = { it.toJson() }
                    )
                }
            }
        })
        dialog.show()
    }

    private fun newQuiz(data: NewQuizRepDataDto) {
        currentQuiz = quizs[QuizType.getQuizType(data.topic)]?.get(data.quizNumber - 1)
        currentQuizIndex = data.quizNumber
        currentQuizType = QuizType.getQuizType(data.topic)!!
        binding.gameQuizText.text = currentQuiz?.content
        binding.gameQuizLayout.visibility = View.VISIBLE

        // 연습문제
        if(data.topic == QuizType.PRACTICE.toString()) {
            binding.imageQuizLayout.visibility = View.VISIBLE
            binding.shortAnswerQuizLayout.visibility = View.INVISIBLE
            binding.multipleChoiceQuizLayout.visibility = View.INVISIBLE
            Glide.with(this).load(currentQuiz?.options?.get(0)).into(binding.imageQuizImage1)
            Glide.with(this).load(currentQuiz?.options?.get(1)).into(binding.imageQuizImage2)
            Glide.with(this).load(currentQuiz?.options?.get(2)).into(binding.imageQuizImage3)
            Glide.with(this).load(currentQuiz?.options?.get(3)).into(binding.imageQuizImage4)
        }
        // 패자부활전
        else if (data.topic == QuizType.REDEMPTION.toString()) {
            binding.imageQuizLayout.visibility = View.INVISIBLE
            binding.shortAnswerQuizLayout.visibility = View.INVISIBLE
            binding.multipleChoiceQuizLayout.visibility = View.VISIBLE
            var optionStr = ""
            currentQuiz?.options?.forEachIndexed { idx, str ->
                optionStr += if(idx == 3) "${idx + 1}. $str"
                else "${idx + 1}. $str\n\n"
            }
            binding.gameMultipleChoiceText.text = optionStr
        }
        // 일반 문제
        else {
            binding.imageQuizLayout.visibility = View.INVISIBLE
            binding.shortAnswerQuizLayout.visibility = View.VISIBLE
            binding.multipleChoiceQuizLayout.visibility = View.INVISIBLE
            (binding.gameWrongAnswerList.adapter as WrongAnswerAdapter).clearMessage()

            startTimer(30) {
                if (it == 15) {
                    val dialog = TimerDialog(this, TimerDialog.Type.HINT, currentQuiz!!.hint!!)
                    dialog.show()
                } else if (it == 0) {
                    if(currentQuizIndex == getRoomInfo()!!.quizCount / 2) {
                        redemption()
                    } else if(currentQuizIndex == getRoomInfo()!!.quizCount) {
                        if(getRoomInfo()!!.isHost)
                            gameOver()
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            SocketClient.sendRequestOnly(
                                request = NewQuizReqDto(
                                    roomId = getRoomInfo()!!.roomId,
                                    quizType = quizType.toString()
                                ),
                                toJson = { it.toJson() }
                            )
                        }
                    }
                }
            }
        }
        binding.footerSendButton.isEnabled = data.userStatus
    }

    private fun leaveRoom() {
        val exitDialog = ExitDialog(this, !isGameStart)
        exitDialog.setOnExitListener(object :ExitDialog.OnExitListener {
            override fun exit() {
                CoroutineScope(Dispatchers.IO).launch {
                    SocketClient.sendRequestOnly(
                        request = LeaveRoomReqDto(
                            userId = getUserId()!!,
                            roomId = getRoomInfo()!!.roomId
                        ),
                        toJson = { it.toJson() }
                    )
                }
                broadcastReceiver.stopListening()
                this@GameActivity.finish()
            }
        })
        exitDialog.show()
    }

    override fun onBackPressed() {
        leaveRoom()
    }

}