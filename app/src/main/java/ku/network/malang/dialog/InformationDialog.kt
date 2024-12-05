package ku.network.malang.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import ku.network.malang.databinding.DialogInformationBinding

class InformationDialog(context: Context, private val type: Type, private val data: String?): Dialog(context) {

    enum class Type {
        PRACTICE_QUIZ,
        GAME_START,
        CONSOLATION_MATCH_START,
        CONSOLATION_MATCH_RESULT,
        GAME_OVER
    }

    private lateinit var binding: DialogInformationBinding
    private val title = arrayOf("연습 문제 나가신다", "게임을 시작하지", "마지막 기회... 패자부활전", "축하한달랑달랑", "게임이 끝났다")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(380.dpToPx(context), 323.dpToPx(context))
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0.6f)

        binding.dialogInformationTitle.text = title[type.ordinal]
        binding.dialogInformationContent.text = Html.fromHtml(getContent(), Html.FROM_HTML_MODE_LEGACY)
    }

    private fun getContent(): String {
        return when(type) {
            Type.PRACTICE_QUIZ ->
                "<font color='#FFA500'>퀴즈 주제 선택권</font>이<br>걸려있다! 맞추자!"
            Type.GAME_START ->
                "이번 대결은 <font color='#FFA500'>$data</font>다!"
            Type.CONSOLATION_MATCH_START ->
                "<font color='#FFA500'>단 1명</font>만 살아남는다."
            Type.CONSOLATION_MATCH_RESULT ->
                "<font color='#FFA500'>$data</font><br>살아남았다!"
            Type.GAME_OVER ->
                "<font color='#FFA500'>말랑말랑 두뇌</font>의<br>소유자는??"
        }
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

}