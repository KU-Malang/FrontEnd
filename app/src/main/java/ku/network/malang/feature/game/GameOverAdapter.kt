package ku.network.malang.feature.game

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ku.network.malang.databinding.ItemGameOverBinding
import ku.network.malang.dto.response.GameResultRepDataDto

class GameOverAdapter(private val items: List<GameResultRepDataDto.User>) : RecyclerView.Adapter<GameOverAdapter.GameOverAdapterViewHolder>() {

    inner class GameOverAdapterViewHolder(private val binding: ItemGameOverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            if(position + 1 < items.size) {
                binding.dialogGameOverIdx.text = (position + 2).toString()
                binding.dialogGameOverNickname.text = items[position + 1].nickname
                binding.dialogGameOverChange.text = items[position + 1].change
                if(items[position + 1].change[0] == '+')
                    binding.dialogGameOverChange.setTextColor(Color.parseColor("#4169E1"))
                else
                    binding.dialogGameOverChange.setTextColor(Color.parseColor("#FF6F61"))
            } else {
                binding.dialogGameOverIdx.text = ""
                binding.dialogGameOverNickname.text = ""
                binding.dialogGameOverChange.text = ""
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameOverAdapterViewHolder {
        val binding = ItemGameOverBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GameOverAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameOverAdapterViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = 7

}