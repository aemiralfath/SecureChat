package com.aemiralfath.securechat.observer

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button

class MyButtonObserver(private val button: Button): TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        button.isEnabled = p0.toString().trim().isNotEmpty()
    }

    override fun afterTextChanged(p0: Editable?) {
    }
}