package ku.network.malang.feature.game

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ku.network.malang.R
import ku.network.malang.dto.response.EnterRoomRepDataDto

class PlayerAdapter(var players: List<EnterRoomRepDataDto.User>) :
    RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    inner class PlayerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playerName: TextView = view.findViewById(R.id.player_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_player, parent, false)
        return PlayerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        if (position < players.size) {
            if(!players[position].isActivated) {
                holder.playerName.setTextColor(Color.parseColor("#A8A8A8"))
            }
            holder.playerName.text = players[position].userName
        }
        else
            holder.playerName.text = ""
    }

    override fun getItemCount(): Int = 8

    fun updatePlayers(newPlayers: List<EnterRoomRepDataDto.User>) {
        players = newPlayers
        notifyDataSetChanged()
    }

}