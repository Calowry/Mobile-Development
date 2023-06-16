package com.project.calowry_app.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.project.calowry_app.R
import com.project.calowry_app.databinding.ActivitySplashBinding
import com.project.calowry_app.ui.MainActivity
import com.project.calowry_app.ui.login.LoginActivity
import com.project.calowry_app.ui.welcome.WelcomeActivity
import com.project.calowry_app.utils.ConstVal
import com.project.calowry_app.utils.SessionManager

class SplashActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null

    private val binding get() = _binding!!

    private lateinit var pref: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        initUI()
    }

    private fun initUI() {
        pref = SessionManager(this)
        val isLogin = pref.isLogin
        Handler(Looper.getMainLooper()).postDelayed({
            when {
                isLogin -> {
                    val imageLogo = findViewById<ImageView>(R.id.calowry_logo)
                    imageLogo.alpha = 1f
                    imageLogo.animate().setDuration(1000).alpha(0f).withEndAction {
                        val intentSplash = Intent(this, MainActivity::class.java)
                        startActivity(intentSplash)
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        finish()
                    }
                }

                else -> {
                    val imageLogo = findViewById<ImageView>(R.id.calowry_logo)
                    imageLogo.alpha = 1f
                    imageLogo.animate().setDuration(1000).alpha(0f).withEndAction {
                        val intentSplash = Intent(this, WelcomeActivity::class.java)
                        startActivity(intentSplash)
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        finish()
                    }
                }
            }
        }, ConstVal.LOADING_TIME)
    }
}