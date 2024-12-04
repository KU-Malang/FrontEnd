package ku.network.malang.feature.lobby

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ku.network.malang.R
import ku.network.malang.model.LobbyItem

class LobbyAdapter(
    private val items: MutableList<LobbyItem>,
    private val onItemClick: (LobbyItem) -> Unit
) : RecyclerView.Adapter<LobbyAdapter.LobbyViewHolder>() {

    inner class LobbyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.lobby_item_title)
        val totalQuestions: TextView = view.findViewById(R.id.lobby_item_total_questions)
        val progress: TextView = view.findViewById(R.id.lobby_item_progress)

        fun bind(item: LobbyItem) {
            val context = itemView.context

            title.text = item.roomName
            totalQuestions.text = context.getString(R.string.quiz_count_format, item.quizCount)
            progress.text = context.getString(R.string.progress_format, item.currentPlayers, item.maxPlayers)
            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LobbyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lobby_card, parent, false)
        return LobbyViewHolder(view)
    }

    override fun onBindViewHolder(holder: LobbyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<LobbyItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
