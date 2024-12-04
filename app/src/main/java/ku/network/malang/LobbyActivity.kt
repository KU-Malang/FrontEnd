package ku.network.malang

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ku.network.malang.databinding.ActivityLobbyBinding
import ku.network.malang.feature.lobby.CreateRoomInteractor
import ku.network.malang.feature.lobby.CreateRoomRepository
import ku.network.malang.feature.lobby.EnterRoomInteractor
import ku.network.malang.feature.lobby.EnterRoomRepository
import ku.network.malang.feature.lobby.LobbyAdapter
import ku.network.malang.feature.lobby.LobbyInteractor
import ku.network.malang.feature.lobby.LobbyRepository
import ku.network.malang.model.LobbyItem

class LobbyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLobbyBinding
    private lateinit var adapter: LobbyAdapter
    private val lobbyInteractor = LobbyInteractor(LobbyRepository())
    private val createRoomInteractor = CreateRoomInteractor(CreateRoomRepository())
    private val enterRoomInteractor = EnterRoomInteractor(EnterRoomRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLobbyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView 설정
        adapter = LobbyAdapter(mutableListOf()) { lobbyItem ->
            enterRoom(lobbyItem) // 클릭 이벤트 처리
        }
        binding.lobbyRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@LobbyActivity)
            adapter = this@LobbyActivity.adapter
        }

        // + 버튼 클릭 시 다이얼로그 표시
        binding.lobbyAddButton.setOnClickListener {
            showCreateRoomDialog()
        }

        // 로비 데이터 가져오기
        val userId = MalangApplication.getUserId()
        if (userId != null) {
            fetchLobbyData(userId = userId)
        }
    }

    private fun fetchLobbyData(userId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = lobbyInteractor.fetchLobbyData(userId)
            withContext(Dispatchers.Main) {
                result.fold(
                    onSuccess = { (items, nickname, rating) ->
                        binding.lobbyUserName.text = nickname
                        binding.lobbyUserScore.text = "$rating 점"
                        adapter.updateItems(items) // RecyclerView에 데이터 추가
                    },
                    onFailure = { error ->
                        Toast.makeText(this@LobbyActivity, error.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    // 방 생성 다이얼로그 표시 함수
    private fun showCreateRoomDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_create_room, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // 다이얼로그 내부 뷰 참조
        val roomNameEditText: EditText = dialogView.findViewById(R.id.create_room_name_et)
        val playersEditText: EditText = dialogView.findViewById(R.id.create_room_players_et)
        val questionsEditText: EditText = dialogView.findViewById(R.id.create_room_questions_et)

        // 만들기 버튼 클릭 이벤트
        dialogView.findViewById<android.widget.Button>(R.id.create_room_button).setOnClickListener {
            val roomName = roomNameEditText.text.toString()
            val players = playersEditText.text.toString()
            val questions = questionsEditText.text.toString()

            // 입력값 유효성 검사
            if (roomName.isEmpty() || players.isEmpty() || questions.isEmpty()) {
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                createRoom(roomName, players, questions)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    // 방 생성 로직
    private fun createRoom(roomName: String, players: String, questions: String) {
        val maxPlayers = players.toIntOrNull()
        val quizCount = questions.toIntOrNull()
        val hostUserId = MalangApplication.getUserId()

        // 입력값 유효성 검사
        if (maxPlayers == null || quizCount == null || hostUserId == null) {
            Toast.makeText(this, "유효하지 않은 입력값입니다.", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val result = createRoomInteractor.createRoom(roomName, maxPlayers, hostUserId, quizCount)

            withContext(Dispatchers.Main) {
                result.fold(
                    onSuccess = { (roomId, message) ->
                        Toast.makeText(this@LobbyActivity, message, Toast.LENGTH_SHORT).show()
                        fetchLobbyData(hostUserId)
                        enterRoom(LobbyItem(roomId, roomName,  quizCount, 1, maxPlayers))
                    },
                    onFailure = { error ->
                        Toast.makeText(this@LobbyActivity, error.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    private fun enterRoom(lobbyItem: LobbyItem) {
        val userId = MalangApplication.getUserId()

        if (userId != null) {
            CoroutineScope(Dispatchers.IO).launch {
                val result = enterRoomInteractor.enterRoom(userId, lobbyItem.roomId)
                withContext(Dispatchers.Main) {
                    result.fold(
                        onSuccess = { message ->
                            Toast.makeText(this@LobbyActivity, "방 입장 성공: $message", Toast.LENGTH_SHORT).show()
                            MalangApplication.setRoomInfo(lobbyItem) //방 정보 저장
                            navigateToGame()
                        },
                        onFailure = { error ->
                            Toast.makeText(this@LobbyActivity, "방 입장 실패: ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        } else {
            Toast.makeText(this, "유저 정보가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToGame() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
        finish()
    }
}
