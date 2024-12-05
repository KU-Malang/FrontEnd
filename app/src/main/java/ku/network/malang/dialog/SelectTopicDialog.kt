package ku.network.malang.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import ku.network.malang.databinding.DialogSelectTopicBinding

class SelectTopicDialog(context: Context) : Dialog(context) {

    interface OnSelectListener {
        fun select(id: Int)
    }

    private lateinit var binding: DialogSelectTopicBinding
    private var onSelectListener: OnSelectListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogSelectTopicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(380.dpToPx(context), 544.dpToPx(context))
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0.6f)
        setCancelable(false)

        binding.dialogSelectTopicBtn.setOnClickListener {
            onSelectListener?.select(binding.dialogSelectTopicRadioGroup.checkedRadioButtonId)
            this@SelectTopicDialog.dismiss()
        }
    }

    fun setOnSelectListener(listener: OnSelectListener) {
        onSelectListener = listener
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

}