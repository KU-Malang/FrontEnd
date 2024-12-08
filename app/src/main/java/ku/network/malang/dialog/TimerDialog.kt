package ku.network.malang.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import ku.network.malang.databinding.DialogTimerBinding

class TimerDialog(context: Context, private val type: Type, private val content: String) : Dialog(context) {

    enum class Type {
        ANSWER, HINT
    }

    interface OnTimeOutListener {
        fun timeOut()
    }

    private lateinit var binding: DialogTimerBinding
    private val title = arrayOf("문제가 풀렸다", "힌트 나가신다")
    private var onTimeOutListener : OnTimeOutListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(380.dpToPx(context), 384.dpToPx(context))
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0.6f)
        if(type == Type.ANSWER)
            setCancelable(false)

        binding.dialogTimerTitle.text = title[type.ordinal]
        binding.dialogTimerContent.text = content
        startCountdown()
    }

    private fun startCountdown() {
        val handler = Handler(Looper.getMainLooper())
        var secondsRemaining = 5

        val runnable = object : Runnable {
            override fun run() {
                if (secondsRemaining > 0) {
                    binding.dialogTimerTime.text = "${secondsRemaining}초"
                    secondsRemaining--
                    handler.postDelayed(this, 1000)
                } else {
                    onTimeOutListener?.timeOut()
                    dismiss()
                }
            }
        }
        handler.post(runnable)
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    fun setOnTimeOutListener(listener: OnTimeOutListener) {
        onTimeOutListener = listener
    }

}