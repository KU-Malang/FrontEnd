package ku.network.malang.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ku.network.malang.R
import ku.network.malang.databinding.ActivityLoginBinding
import ku.network.malang.feature.login.LoginInteractor
import ku.network.malang.feature.login.LoginRepository

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginInteractor by lazy { LoginInteractor(LoginRepository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // EditText와 글자 수 표시 TextView 참조
        val usernameEditText: EditText = findViewById(R.id.login_username_et)
        val passwordEditText: EditText = findViewById(R.id.login_password_et)
        val usernameCountTextView: TextView = findViewById(R.id.login_username_count_tv)

        // TextWatcher 추가
        usernameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 글자 수 계산 및 표시
                val currentLength = s?.length ?: 0
                usernameCountTextView.text = "$currentLength/7"
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    println("닉네임을 입력하세요")
                } else if (s.length > 7) {
                    println("최대 7자 입력이 가능합니다")
                }
            }
        })

        // 로그인 버튼 클릭 이벤트
        binding.loginBtn.setOnClickListener {
            val nickname = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (nickname.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "닉네임과 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
            } else {
                performLogin(nickname, password)
            }
        }

    }

    private fun performLogin(nickname: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = loginInteractor.performLogin(nickname, password)
            withContext(Dispatchers.Main) {
                result.fold(
                    onSuccess = { message ->
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                        navigateToLobby()
                    },
                    onFailure = { error ->
                        Toast.makeText(this@LoginActivity, "로그인 실패: ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
                        binding.loginUsernameEt.text.clear()
                        binding.loginPasswordEt.text.clear()
                    }
                )
            }
        }
    }

    private fun navigateToLobby() {
        val intent = Intent(this, LobbyActivity::class.java)
        startActivity(intent)
        finish()
    }

}
