package ku.network.malang

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ku.network.malang.databinding.ActivityGameBinding
import ku.network.malang.feature.game.PlayerAdapter

class GameActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO : 데이터 예제
        val players = listOf(
            "Player 1", "Player 2", "Player 3", "Player 4",
            "Player 5", "Player 6", "Player 7", "Player 8"
        )

        // RecyclerView 설정
        val recyclerView: RecyclerView = findViewById(R.id.game_player_list)
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.adapter = PlayerAdapter(players)
    }

}