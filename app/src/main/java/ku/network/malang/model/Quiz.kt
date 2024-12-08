package ku.network.malang.model

data class Quiz(
    val content: String,
    val hint: String?,
    val options: List<String>?,
    val answer: String
)
