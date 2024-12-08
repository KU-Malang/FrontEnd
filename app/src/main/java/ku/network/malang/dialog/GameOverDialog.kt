package ku.network.malang.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ku.network.malang.databinding.DialogGameOverBinding
import ku.network.malang.dto.response.GameResultRepDataDto
import ku.network.malang.feature.game.GameOverAdapter

class GameOverDialog(context: Context, private val items: List<GameResultRepDataDto.User>): Dialog(context) {

    interface OnExitListener {
        fun exit()
    }

    private lateinit var binding: DialogGameOverBinding
    private var onExitListener : OnExitListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(380.dpToPx(context), 630.dpToPx(context))
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0.6f)
        setCancelable(false)

        binding.winnerText.text = items[0].nickname

        binding.gameOverPlayerList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.gameOverPlayerList.adapter = GameOverAdapter(items)

        binding.dialogGameOverBtn.setOnClickListener {
            onExitListener?.exit()
            this@GameOverDialog.dismiss()
        }
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    fun setOnExitListener(listener: OnExitListener) {
        onExitListener = listener
    }

}