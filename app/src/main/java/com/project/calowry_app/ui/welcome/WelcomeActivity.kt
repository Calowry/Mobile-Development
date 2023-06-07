package com.project.calowry_app.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import com.project.calowry_app.R
import com.project.calowry_app.ui.choosing.ChoosingActivity

class WelcomeActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val nextBtn: ImageButton = findViewById(R.id.next_btn)
        nextBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.next_btn -> {
                val intent = Intent(this, ChoosingActivity::class.java)
                startActivity(intent)
            }
        }
    }
}