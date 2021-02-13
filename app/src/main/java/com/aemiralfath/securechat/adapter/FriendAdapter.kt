package com.aemiralfath.securechat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aemiralfath.securechat.CustomOnItemClickListener
import com.aemiralfath.securechat.R
import com.aemiralfath.securechat.databinding.ItemFriendBinding
import com.aemiralfath.securechat.entity.Friend
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FriendAdapter : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {
    var listFriends = ArrayList<Friend>()
        set(listFriends) {
            if (listFriends.size > 0) {
                this.listFriends.clear()
            }
            this.listFriends.addAll(listFriends)
            notifyDataSetChanged()
        }

    fun addItem(friend: Friend) {
        this.listFriends.add(friend)
        notifyItemInserted(this.listFriends.size - 1)
    }

    fun updateItem(position: Int, friend: Friend) {
        this.listFriends[position] = friend
        notifyItemChanged(position, friend)
    }

    fun removeItem(position: Int) {
        this.listFriends.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFriends.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(listFriends[position])
    }

    override fun getItemCount(): Int = this.listFriends.size

    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemFriendBinding.bind(itemView)
        fun bind(friend: Friend) {
            binding.tvItemFriendName.text = friend.name
            Glide.with(itemView.context).load(friend.profileImage)
                .placeholder(R.drawable.ic_baseline_person_24)
                .apply(RequestOptions().override(55, 55)).into(binding.imgItemFriendPhoto)

            binding.btnChat.setOnClickListener(
                View.OnClickListener {

                }
            )

            binding.cvItemFriend.setOnClickListener(
                CustomOnItemClickListener(
                    adapterPosition,
                    object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(view: View, position: Int) {
                            TODO("Not yet implemented")
                        }
                    }
                )
            )
        }
    }
}