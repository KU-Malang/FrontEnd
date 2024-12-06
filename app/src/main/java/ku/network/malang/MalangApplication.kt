package ku.network.malang

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ku.network.malang.model.LobbyItem
import ku.network.malang.model.Quiz
import ku.network.malang.model.QuizType
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.EnumMap
import java.util.HashMap

class MalangApplication : Application() {

    companion object {
        @Volatile
        private var userId: Int? = null

        @Volatile
        private var roomInfo: LobbyItem? = null

        @Volatile
        lateinit var quizs : EnumMap<QuizType, List<Quiz>>

        @Synchronized
        fun setUserId(id: Int) {
            userId = id
        }

        fun getUserId(): Int? {
            return userId
        }

        @Synchronized
        fun setRoomInfo(room: LobbyItem) {
            roomInfo = room
        }

        fun getRoomInfo(): LobbyItem? {
            return roomInfo
        }
    }

    override fun onCreate() {
        super.onCreate()
        quizs = EnumMap(QuizType::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            quizs[QuizType.POPULAR_CULTURE] = parseCsvFile(R.raw.popular_culture)
            quizs[QuizType.HISTORY] = parseCsvFile(R.raw.history)
            quizs[QuizType.CURRENT_AFFAIRS] = parseCsvFile(R.raw.current_affairs)
            quizs[QuizType.GEOGRAPHY] = parseCsvFile(R.raw.geography)
            quizs[QuizType.COMPUTER] = parseCsvFile(R.raw.computer)
            quizs[QuizType.GENERAL_KNOWLEDGE] = parseCsvFile(R.raw.general_knowledge)
            quizs[QuizType.PRACTICE] = parseCsvFile(R.raw.practice)
            quizs[QuizType.REDEMPTION] = parseCsvFile(R.raw.redemption)
        }
    }

    private fun parseCsvFile(rawResId: Int): List<Quiz> {
        val inputStream = this.resources.openRawResource(rawResId)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val quizList = mutableListOf<Quiz>()
        reader.useLines { lines ->
            lines.drop(1).forEach { line ->
                val tokens = line.split(",")
                if (tokens.size >= 6) {
                    quizList.add(
                        Quiz(
                            content = tokens[0],
                            null,
                            options = tokens.subList(1, 5),
                            answer = tokens[5]
                        )
                    )
                } else if (tokens.size >= 3) {
                    quizList.add(Quiz(
                        content = tokens[0],
                        hint = tokens[1],
                        null,
                        answer = tokens[2]
                    ))
                }
            }
        }
        return quizList
    }

}
