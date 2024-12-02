package ku.network.malang.network

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket

object SocketClient {
    private const val SERVER_IP = "192.168.0.1" // 서버 IP
    private const val SERVER_PORT = 8080       // 서버 포트

    private var socket: Socket? = null
    private var output: PrintWriter? = null
    private var input: BufferedReader? = null

    @Synchronized
    private fun initializeConnection() {
        if (socket == null || socket?.isClosed == true) {
            socket = Socket(SERVER_IP, SERVER_PORT)
            output = PrintWriter(OutputStreamWriter(socket!!.getOutputStream()), true)
            input = BufferedReader(InputStreamReader(socket!!.getInputStream()))
        }
    }

    fun <T, R> sendRequest(
        request: T,
        toJson: (T) -> String,
        fromJson: (String) -> R
    ): R? {
        return try {
            // 연결 초기화 (필요 시만)
            initializeConnection()

            // 요청 전송
            output?.println(toJson(request))

            // 응답 수신
            val responseString = input?.readLine()
            if (responseString != null) {
                fromJson(responseString)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @Synchronized
    fun closeConnection() {
        try {
            socket?.close()
            socket = null
            output = null
            input = null
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}