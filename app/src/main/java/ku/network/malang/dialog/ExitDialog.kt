package ku.network.malang.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import ku.network.malang.databinding.DialogExitBinding

class ExitDialog(context: Context, private val beforeGame: Boolean) : Dialog(context) {

    interface OnExitListener {
        fun exit()
    }

    private lateinit var binding: DialogExitBinding
    private var onExitListener : OnExitListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogExitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(380.dpToPx(context), 355.dpToPx(context))
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0.6f)

        if(beforeGame) {
            binding.dialogExitContent.text = "다시 생각해봐~"
        }

        binding.dialogExitBtn.setOnClickListener {
            onExitListener?.exit()
            this@ExitDialog.dismiss()
        }
    }

    fun setOnExitListener(listener: OnExitListener) {
        this.onExitListener = listener
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

}