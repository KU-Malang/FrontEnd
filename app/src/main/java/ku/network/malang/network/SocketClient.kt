package ku.network.malang.network

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket

object SocketClient {
    private const val SERVER_IP = "192.168.0.1" // 서버 IP
    private const val SERVER_PORT = 8080       // 서버 포트

    // 제너릭 요청 함수
    fun <T, R> sendRequest(
        request: T,
        toJson: (T) -> String,
        fromJson: (String) -> R
    ): R? {
        var socket: Socket? = null
        return try {
            // 서버와 연결
            socket = Socket(SERVER_IP, SERVER_PORT)

            // 요청 전송
            val output = PrintWriter(OutputStreamWriter(socket.getOutputStream()), true)
            output.println(toJson(request)) // 요청 DTO를 JSON으로 변환하여 전송

            // 응답 수신
            val input = BufferedReader(InputStreamReader(socket.getInputStream()))
            val responseString = input.readLine()

            // JSON 응답을 DTO로 변환하여 반환
            fromJson(responseString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            socket?.close()
        }
    }
}