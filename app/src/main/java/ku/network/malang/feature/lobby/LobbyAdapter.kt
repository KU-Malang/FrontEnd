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
            title.text = item.title
            totalQuestions.text = item.totalQuestions
            progress.text = item.progress
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
        val item = items[position]
        holder.title.text = item.title
        holder.totalQuestions.text = item.totalQuestions
        holder.progress.text = item.progress
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<LobbyItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
