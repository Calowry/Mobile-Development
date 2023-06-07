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
import com.project.calowry_app.ui.welcome.WelcomeActivity

class SplashActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        initUI()
    }

    private fun initUI() {
        Handler(Looper.getMainLooper()).postDelayed({
            val imageLogo = findViewById<ImageView>(R.id.calowry_logo)
            imageLogo.alpha = 1f
            imageLogo.animate().setDuration(1000).alpha(0f).withEndAction {
                val intentSplash = Intent(this, WelcomeActivity::class.java)
                startActivity(intentSplash)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }, 1500L)
    }
}