package ku.network.malang.feature.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ku.network.malang.databinding.ItemWrongAnswerBinding

class WrongAnswerAdapter : RecyclerView.Adapter<WrongAnswerAdapter.WrongAnswerViewHolder>() {

    private val messages: MutableList<WrongAnswerItem> = mutableListOf()

    data class WrongAnswerItem(val nickname: String, val message: String)

    class WrongAnswerViewHolder(private val binding: ItemWrongAnswerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WrongAnswerItem) {
            binding.itemWrongAnswerNickname.text = item.nickname
            binding.itemWrongAnswerText.text = item.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WrongAnswerViewHolder {
        val binding = ItemWrongAnswerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WrongAnswerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WrongAnswerViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(nickname: String, message: String) {
        messages.add(WrongAnswerItem(nickname, message))
        notifyItemInserted(messages.size - 1)
    }

    fun clearMessage() {
        messages.clear()
        notifyDataSetChanged()
    }

}