package com.project.calowry_app.ui.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import com.project.calowry_app.R.string
import com.project.calowry_app.database.api.ApiResponse
import com.project.calowry_app.database.auth.AuthBody
import com.project.calowry_app.databinding.ActivityRegisterBinding
import com.project.calowry_app.ui.login.LoginActivity
import com.project.calowry_app.utils.ConstVal
import com.project.calowry_app.utils.isEmailValid
import com.project.calowry_app.utils.showOKDialog
import com.project.calowry_app.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val registerViewModel: RegisterViewModel by viewModels()

    private var _activityRegisterBinding: ActivityRegisterBinding? = null
    private val binding get() = _activityRegisterBinding!!

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(_activityRegisterBinding?.root)

        initAction()

    }

    private fun initAction() {
        binding.registerBtn.setOnClickListener {
            val userName = binding.NameEditText.text.toString()
            val userEmail = binding.EmailEditText.text.toString()
            val userPassword = binding.PasswordEditText.text.toString()

            Handler(Looper.getMainLooper()).postDelayed({
                when {
                    userName.isBlank() -> binding.NameEditText.error = getString(string.name_is_empty)
                    userEmail.isBlank() -> binding.EmailEditText.error = getString(string.email_is_empty)
                    !userEmail.isEmailValid() -> binding.EmailEditText.error = getString(string.email_is_invalid)
                    userPassword.isBlank() -> binding.PasswordEditText.error = getString(string.password_is_empty)
                    userPassword.length < 8 -> binding.PasswordEditText.error = getString(string.password_is_invalid)
                    else -> {
                        val request = AuthBody(
                            userName, userEmail, userPassword
                        )
                        registerUser(request)
                    }
                }
            }, ConstVal.ACTION_DELAYED_TIME)
        }
        binding.loginHere.setOnClickListener {
            LoginActivity.start(this)
        }
    }

    private fun registerUser(newUser: AuthBody) {
        registerViewModel.registerUser(newUser).observe(this) { response ->
            when(response) {
                is ApiResponse.Loading -> {
                    showLoading(true)
                }
                is ApiResponse.Success -> {
                    try {
                        showLoading(false)
                    } finally {
                        LoginActivity.start(this)
                        finish()
                        showToast(getString(string.message_register_success))
                    }
                }
                is ApiResponse.Error -> {
                    showLoading(false)
                    showOKDialog(getString(string.title_dialog_error), response.errorMessage)
                }
                else -> {
                    showToast(getString(string.message_unknown_state))
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.bgDim.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.NameEditText.isClickable = !isLoading
        binding.NameEditText.isEnabled = !isLoading
        binding.EmailEditText.isClickable = !isLoading
        binding.EmailEditText.isEnabled = !isLoading
        binding.PasswordEditText.isClickable = !isLoading
        binding.PasswordEditText.isEnabled = !isLoading
        binding.registerBtn.isClickable = !isLoading
    }
}