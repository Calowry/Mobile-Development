package com.project.calowry_app.ui.choosing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import com.project.calowry_app.R
import com.project.calowry_app.databinding.ActivityChoosingBinding
import com.project.calowry_app.databinding.ActivityMainBinding
import com.project.calowry_app.databinding.ActivityWelcomeBinding
import com.project.calowry_app.ui.login.LoginActivity
import com.project.calowry_app.ui.register.RegisterActivity

class ChoosingActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choosing)

        val loginBtn: Button = findViewById(R.id.login_btn)
        loginBtn.setOnClickListener(this)

        val registerBtn: Button = findViewById(R.id.register_btn)
        registerBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login_btn -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

            R.id.register_btn -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}