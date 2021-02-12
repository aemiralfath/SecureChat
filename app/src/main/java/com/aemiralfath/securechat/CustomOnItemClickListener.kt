package com.aemiralfath.securechat

import android.view.View

class CustomOnItemClickListener(
    private val position: Int,
    private val onItemClickCallback: OnItemClickCallback
) : View.OnClickListener {
    override fun onClick(p0: View?) {
        p0?.let { onItemClickCallback.onItemClicked(it, position) }
    }

    interface OnItemClickCallback {
        fun onItemClicked(view: View, position: Int)
    }
}