package com.aemiralfath.securechat.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aemiralfath.securechat.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        supportActionBar?.title = "About"
    }
}