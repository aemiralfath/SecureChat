package com.aemiralfath.securechat.observer

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aemiralfath.securechat.MessageViewHolder

class MyScrollToBottomObserver(
    private val recycler: RecyclerView,
    private val adapter: RecyclerView.Adapter<MessageViewHolder>,
    private val manager: LinearLayoutManager
) : RecyclerView.AdapterDataObserver() {

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
        val count = adapter.itemCount
        val lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition()

        val loading = lastVisiblePosition == -1
        val atBottom = positionStart >= (count-1) && lastVisiblePosition == (positionStart -1)
        if (loading || atBottom) {
            recycler.scrollToPosition(positionStart)
        }
    }
}