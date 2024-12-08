package ku.network.malang.model

enum class QuizType(private val displayName: String, val koreanName: String) {

    POPULAR_CULTURE("POPULAR_CULTURE", "대중문화"),
    HISTORY("HISTORY", "역사"),
    CURRENT_AFFAIRS("CURRENT_AFFAIRS", "시사"),
    GEOGRAPHY("GEOGRAPHY", "지리"),
    COMPUTER("COMPUTER", "컴퓨터"),
    GENERAL_KNOWLEDGE("GENERAL_KNOWLEDGE", "상식"),
    PRACTICE("PRACTICE", "연습문제"),
    REDEMPTION("REDEMPTION", "패자부활전");

    override fun toString(): String {
        return displayName
    }

    companion object {
        fun getQuizType(str: String): QuizType? {
            return values().find { it.displayName == str }
        }
    }

}