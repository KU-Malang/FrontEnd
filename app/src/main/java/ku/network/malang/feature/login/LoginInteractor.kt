package ku.network.malang.feature.login

import ku.network.malang.MalangApplication

class LoginInteractor(private val repository: LoginRepository) {
    fun performLogin(nickname: String, password: String): Result<String> {
        val response = repository.login(nickname, password)

        return if (response != null) {
            when (response.status) {
                "success" -> {
                    response.userId?.let { MalangApplication.setUserId(it) }
                    Result.success("로그인 성공! User ID: ${response.userId}")
                }
                "10001" -> Result.failure(Exception("오류: 글자 수를 초과하였습니다."))
                "10002" -> Result.failure(Exception("오류: 비밀번호가 일치하지 않습니다."))
                "10003" -> Result.failure(Exception("오류: 이미 로그인 중인 사용자입니다."))
                else -> Result.failure(Exception("알 수 없는 오류: ${response.message}"))
            }
        } else {
            Result.failure(Exception("서버와의 연결에 실패했습니다."))
        }
    }
}