package ku.network.malang

import android.app.Application

class MalangApplication : Application() {

    companion object {
        @Volatile
        private var userId: Int? = null

        @Volatile
        private var roomId: Int? = null

        @Synchronized
        fun setUserId(id: Int) {
            userId = id
        }

        fun getUserId(): Int? {
            return userId
        }

        @Synchronized
        fun setRoomId(id: Int) {
            roomId = id
        }

        fun getRoomId(): Int? {
            return roomId
        }
    }

    override fun onCreate() {
        super.onCreate()
        // 초기화 작업이 필요하면 여기에 추가
    }
}
