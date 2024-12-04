package ku.network.malang

import android.app.Application
import ku.network.malang.model.LobbyItem

class MalangApplication : Application() {

    companion object {
        @Volatile
        private var userId: Int? = null

        @Volatile
        private var roomInfo: LobbyItem? = null

        @Synchronized
        fun setUserId(id: Int) {
            userId = id
        }

        fun getUserId(): Int? {
            return userId
        }

        @Synchronized
        fun setRoomInfo(room: LobbyItem) {
            roomInfo = room
        }

        fun getRoomInfo(): LobbyItem? {
            return roomInfo
        }
    }

    override fun onCreate() {
        super.onCreate()
        // 초기화 작업이 필요하면 여기에 추가
    }
}
