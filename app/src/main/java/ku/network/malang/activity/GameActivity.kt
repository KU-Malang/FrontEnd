package ku.network.malang.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ku.network.malang.MalangApplication.Companion.getRoomInfo
import ku.network.malang.MalangApplication.Companion.getUserId
import ku.network.malang.R
import ku.network.malang.databinding.ActivityGameBinding
import ku.network.malang.dialog.ExitDialog
import ku.network.malang.dialog.InformationDialog
import ku.network.malang.dialog.SelectTopicDialog
import ku.network.malang.dialog.TimerDialog
import ku.network.malang.dto.request.GameStartReqDto
import ku.network.malang.dto.request.LeaveRoomReqDto
import ku.network.malang.dto.response.CorrectRepDataDto
import ku.network.malang.dto.response.EnterRoomRepDataDto
import ku.network.malang.dto.response.GameResultRepDataDto
import ku.network.malang.dto.response.IncorrectRepDataDto
import ku.network.malang.dto.response.LeaveRoomRepDataDto
import ku.network.malang.dto.response.NewQuizRepDataDto
import ku.network.malang.dto.response.SelectTopicRepDataDto
import ku.network.malang.feature.game.PlayerAdapter
import ku.network.malang.network.BroadcastReceiver
import ku.network.malang.network.SocketClient

class GameActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGameBinding
    private val broadcastReceiver = BroadcastReceiver()
    private var isGameStart = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView 설정
        binding.gamePlayerList.layoutManager = GridLayoutManager(this, 4)
        binding.gamePlayerList.adapter = getRoomInfo()?.let { PlayerAdapter(it.playerList) }

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
//            val dialog = InformationDialog(this, InformationDialog.Type.PRACTICE_QUIZ, "대중문화")
//            dialog.show()

//            val dialog = SelectTopicDialog(this)
//            dialog.setOnSelectListener(object: SelectTopicDialog.OnSelectListener {
//                override fun select(id: Int) {
//                    when(id) {
//                        R.id.radio_common -> Toast.makeText(this@GameActivity, "상식", Toast.LENGTH_SHORT).show()
//                        R.id.radio_computer -> Toast.makeText(this@GameActivity, "컴퓨터", Toast.LENGTH_SHORT).show()
//                        R.id.radio_culture -> Toast.makeText(this@GameActivity, "대중문화", Toast.LENGTH_SHORT).show()
//                        R.id.radio_geography -> Toast.makeText(this@GameActivity, "지리", Toast.LENGTH_SHORT).show()
//                        R.id.radio_history -> Toast.makeText(this@GameActivity, "역사", Toast.LENGTH_SHORT).show()
//                        R.id.radio_current_event -> Toast.makeText(this@GameActivity, "시사", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            })
//            dialog.show()

//            val dialog = TimerDialog(this, TimerDialog.Type.ANSWER, "정답은 경복궁입니다")
//            dialog.show()
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
                }
            }

            override fun newQuizResponse(data: NewQuizRepDataDto?) {
                runOnUiThread {
                    if (data == null) Toast.makeText(this@GameActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
                }
            }

            override fun correctResponse(data: CorrectRepDataDto?) {
                runOnUiThread {
                    if (data == null) Toast.makeText(this@GameActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
                }
            }

            override fun incorrectResponse(data: IncorrectRepDataDto?) {
                runOnUiThread {
                    if (data == null) Toast.makeText(this@GameActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
                }
            }

            override fun gameResultResponse(data: GameResultRepDataDto?) {
                runOnUiThread {
                    if (data == null) Toast.makeText(this@GameActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
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

    private fun gameStart() {
        isGameStart = true
        Toast.makeText(this@GameActivity, "게임 시작!!", Toast.LENGTH_SHORT).show()
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
                this@GameActivity.finish()
            }
        })
        exitDialog.show()
    }

    override fun onBackPressed() {
        leaveRoom()
    }

}