package com.project.calowry_app.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.project.calowry_app.R
import com.project.calowry_app.database.api.ApiResponse
import com.project.calowry_app.database.auth.LoginBody
import com.project.calowry_app.databinding.ActivityLoginBinding
import com.project.calowry_app.ui.MainActivity
import com.project.calowry_app.ui.register.RegisterActivity
import com.project.calowry_app.utils.ConstVal.KEY_ACTIVE
import com.project.calowry_app.utils.ConstVal.KEY_EMAIL
import com.project.calowry_app.utils.ConstVal.KEY_IS_LOGIN
import com.project.calowry_app.utils.ConstVal.KEY_ROLE_ID
import com.project.calowry_app.utils.ConstVal.KEY_TOKEN
import com.project.calowry_app.utils.ConstVal.KEY_USER_ID
import com.project.calowry_app.utils.ConstVal.KEY_USER_NAME
import com.project.calowry_app.utils.ConstVal.KEY_VERIFIED
import com.project.calowry_app.utils.SessionManager
import com.project.calowry_app.utils.gone
import com.project.calowry_app.utils.show
import com.project.calowry_app.utils.showOKDialog
import com.project.calowry_app.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    private var _activityLoginBinding: ActivityLoginBinding? = null
    private val binding get() = _activityLoginBinding!!

    private lateinit var pref: SessionManager

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(_activityLoginBinding?.root)

        pref = SessionManager(this)

        initAction()
    }

    private fun initAction() {
        binding.loginBtn.setOnClickListener {
            val userEmail = binding.EmailEditText.text.toString()
            val userPassword = binding.PasswordEditText.text.toString()

            when {
                userEmail.isBlank() -> {
                    binding.EmailEditText.requestFocus()
                    binding.EmailEditText.error = getString(R.string.password_is_empty)
                }
                userPassword.isBlank() -> {
                    binding.PasswordEditText.requestFocus()
                    binding.PasswordEditText.error = getString(R.string.password_is_empty)
                }
                userPassword.length < 8 ->  {
                    binding.PasswordEditText.requestFocus()
                    binding.PasswordEditText.error = getString(R.string.password_is_invalid)
                }
                else -> {
                    val request = LoginBody(
                        userEmail, userPassword
                    )

                    loginUser(request, userEmail)
                }
            }
        }
        binding.registerHere.setOnClickListener {
            RegisterActivity.start(this)
        }
    }

    private fun loginUser(loginBody: LoginBody, email: String) {
        loginViewModel.loginUser(loginBody).observe(this) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    showLoading(true)
                }
                is ApiResponse.Success -> {
                    try {
                        showLoading(false)
                        val userData = response.data.loginResult
                        userData.let {
                            pref.apply {
                                setStringPreference(KEY_USER_ID, it.id.toString())
                                setStringPreference(KEY_USER_NAME, it.name)
                                setStringPreference(KEY_EMAIL, email)
                                setStringPreference(KEY_ROLE_ID, it.roleId.toString())
                                setStringPreference(KEY_VERIFIED, it.verified)
                                setStringPreference(KEY_ACTIVE, it.active)
                                setStringPreference(KEY_TOKEN, it.token)
                                setBooleanPreference(KEY_IS_LOGIN, true)
                            }
                        }
                        MainActivity.start(this)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        showToast(getString(R.string.title_dialog_error))
                    }
                }
                is ApiResponse.Error -> {
                    showLoading(false)
                    showOKDialog(
                        getString(R.string.title_dialog_error),
                        getString(R.string.auth_is_invalid)
                    )
                }
                else -> {
                    showToast(getString(R.string.message_unknown_state))
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.show() else binding.progressBar.gone()
        if (isLoading) binding.bgDim.show() else binding.bgDim.gone()
        binding.EmailEditText.isClickable = !isLoading
        binding.EmailEditText.isEnabled = !isLoading
        binding.PasswordEditText.isClickable = !isLoading
        binding.PasswordEditText.isEnabled = !isLoading
        binding.loginBtn.isClickable = !isLoading
    }
}