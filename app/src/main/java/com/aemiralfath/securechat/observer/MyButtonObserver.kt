package com.aemiralfath.securechat.observer

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView

class MyButtonObserver(private val button: Button, private val textView: TextView?) : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (textView != null && !textView.text.isNullOrEmpty() && (p0.toString().length == textView.text.toString().length)) {
            button.isEnabled = p0.toString().trim().isNotEmpty()
        } else if (textView == null){
            button.isEnabled = p0.toString().trim().isNotEmpty()
        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }
}