package ku.network.malang.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ku.network.malang.R
import ku.network.malang.databinding.ActivityGameBinding
import ku.network.malang.dialog.ExitDialog
import ku.network.malang.dialog.InformationDialog
import ku.network.malang.dialog.SelectTopicDialog
import ku.network.malang.dialog.TimerDialog
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

        binding.footerSendButton.setOnClickListener {
//            val exitDialog = ExitDialog(this, true)
//            exitDialog.setOnExitListener(object :ExitDialog.OnExitListener {
//                override fun exit() {
//                    this@GameActivity.finish()
//                }
//            })
//            exitDialog.show()

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

}