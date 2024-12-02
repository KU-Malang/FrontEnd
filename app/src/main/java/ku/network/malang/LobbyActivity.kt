package ku.network.malang

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ku.network.malang.databinding.ActivityLobbyBinding
import ku.network.malang.feature.lobby.LobbyAdapter
import ku.network.malang.feature.lobby.LobbyInteractor
import ku.network.malang.feature.lobby.LobbyRepository

class LobbyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLobbyBinding
    private lateinit var adapter: LobbyAdapter
    private val interactor = LobbyInteractor(LobbyRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLobbyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = LobbyAdapter(mutableListOf())
        binding.lobbyRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@LobbyActivity)
            adapter = this@LobbyActivity.adapter
        }

        val userId = MalangApplication.getUserId()
        if (userId != null) {
            fetchLobbyData(userId = userId)
        }
    }

    private fun fetchLobbyData(userId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = interactor.fetchLobbyData(userId)
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
}
