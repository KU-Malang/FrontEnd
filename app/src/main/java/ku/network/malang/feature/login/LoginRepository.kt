package ku.network.malang.feature.login

import ku.network.malang.dto.request.LoginReqDto
import ku.network.malang.dto.response.LoginRepDto
import ku.network.malang.network.SocketClient
import org.json.JSONObject

class LoginRepository {
    fun login(nickname: String, password: String): LoginRepDto? {
        // 요청 DTO 생성
        val loginReqDto = LoginReqDto(
            messageType = 1,
            nickname = nickname,
            password = password
        )

        // 소켓 통신 실행
        return SocketClient.sendRequest(
            request = loginReqDto,
            toJson = { it.toJson() },                // 요청 DTO -> JSON 문자열로 변환
            fromJson = { LoginRepDto.fromJson(JSONObject(it).toString()) } // JSON 문자열 -> 응답 DTO로 변환
        )
    }
}