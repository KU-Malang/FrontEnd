package ku.network.malang.model

enum class QuizType(private val displayName: String) {

    POPULAR_CULTURE("POPULAR_CULTURE"),
    HISTORY("HISTORY"),
    CURRENT_AFFAIRS("CURRENT_AFFAIRS"),
    GEOGRAPHY("GEOGRAPHY"),
    COMPUTER("COMPUTER"),
    GENERAL_KNOWLEDGE("GENERAL_KNOWLEDGE"),
    PRACTICE("PRACTICE"),
    REDEMPTION("REDEMPTION");

    override fun toString(): String {
        return displayName
    }
}