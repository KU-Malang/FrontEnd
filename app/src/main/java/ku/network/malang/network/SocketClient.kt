package ku.network.malang.network

import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket

object SocketClient {
    private const val SERVER_IP = "43.200.215.241"
    private const val SERVER_PORT = 8080
    private const val TIMEOUT = 15000

    private var socket: Socket? = null
    private var output: PrintWriter? = null
    private var input: BufferedReader? = null

    @Synchronized
    private fun initializeConnection() {
        try {
            if (socket == null || socket?.isClosed == true) {
                socket = Socket(SERVER_IP, SERVER_PORT).apply {
                    soTimeout = TIMEOUT
                }
                output = PrintWriter(OutputStreamWriter(socket!!.getOutputStream()), true)
                input = BufferedReader(InputStreamReader(socket!!.getInputStream()))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("SocketClient", "소켓 초기화 실패: ${e.message}")
            throw RuntimeException("서버에 연결할 수 없습니다.")
        }
    }

    fun <T, R> sendRequest(
        request: T,
        toJson: (T) -> String,
        fromJson: (String) -> R
    ): R? {
        return try {
            initializeConnection()

            if (output == null || socket?.isClosed == true) {
                throw IOException("소켓이 열려 있지 않거나 출력 스트림이 null입니다.")
            }
            output?.println(toJson(request))
            Log.d("요청 전송", "전송 데이터: ${toJson(request)}")

            val responseString = input?.readLine()
            if (responseString != null) {
                Log.d("응답 수신", "응답 데이터: ${fromJson(responseString)}")
                fromJson(responseString)
            } else {
                Log.e("SocketClient", "서버 응답이 null입니다.")
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("SocketClient", "요청 처리 실패: ${e.message}")
            null
        }
    }

    fun <T> sendRequestOnly(
        request: T,
        toJson: (T) -> String
    ) {
        try {
            initializeConnection()
            if (output == null || socket?.isClosed == true) {
                throw IOException("소켓이 열려 있지 않거나 출력 스트림이 null입니다.")
            }
            output?.println(toJson(request))
            Log.d("요청 전송", "전송 데이터: ${toJson(request)}")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("SocketClient", "요청 처리 실패: ${e.message}")
        }
    }

    fun startListening(onMessageReceived: (String) -> Unit, onDisconnected: () -> Unit) {
        Thread {
            try {
                initializeConnection()
                while (true) {
                    val message = input?.readLine()
                    if (message != null) {
                        Log.d("브로드캐스트 수신", "수신 데이터: $message")
                        onMessageReceived(message)
                    } else {
                        Log.e("SocketClient", "연결 끊어짐")
                        onDisconnected()
                        break
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("SocketClient", "브로드캐스트 수신 실패: ${e.message}")
            }
        }.start()
    }

    @Synchronized
    fun closeConnection() {
        try {
            output?.close()
            input?.close()
            socket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("SocketClient", "연결 닫기 실패: ${e.message}")
        } finally {
            socket = null
            output = null
            input = null
        }
    }

    fun startListening(onDisconnected: () -> Unit) {

    }
}
