package ku.network.malang.feature.login

class LoginInteractor(private val repository: LoginRepository) {
    fun performLogin(nickname: String, password: String): String {
        val response = repository.login(nickname, password)

        return if (response != null) {
            when (response.status) {
                "success" -> "로그인 성공! User ID: ${response.userId}"
                "10001" -> "오류: 글자 수를 초과하였습니다."
                "10002" -> "오류: 비밀번호가 일치하지 않습니다."
                "10003" -> "오류: 이미 로그인 중인 사용자입니다."
                else -> "알 수 없는 오류: ${response.message}"
            }
        } else {
            "서버와의 연결에 실패했습니다."
        }
    }
}