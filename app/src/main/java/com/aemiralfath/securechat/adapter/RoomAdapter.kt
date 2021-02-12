package com.aemiralfath.securechat.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aemiralfath.securechat.CustomOnItemClickListener
import com.aemiralfath.securechat.R
import com.aemiralfath.securechat.databinding.ItemRoomBinding
import com.aemiralfath.securechat.entity.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class RoomAdapter : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {
    var listRooms = ArrayList<Room>()
        set(listRooms) {
            if (listRooms.size > 0) {
                this.listRooms.clear()
            }
            this.listRooms.addAll(listRooms)
            notifyDataSetChanged()
        }

    fun addItem(room: Room) {
        this.listRooms.add(room)
        notifyItemInserted(this.listRooms.size - 1)
    }

    fun updateItem(position: Int, room: Room) {
        this.listRooms[position] = room
        notifyItemChanged(position, room)
    }

    fun removeItem(position: Int) {
        this.listRooms.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listRooms.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(listRooms[position])
    }

    override fun getItemCount(): Int = this.listRooms.size

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRoomBinding.bind(itemView)
        fun bind(room: Room) {
            binding.tvItemName.text = room.name
            binding.tvItemDate.text = room.lastDate
            binding.tvItemChat.text = room.lastChat
            Glide.with(itemView.context).load(room.image)
                .placeholder(R.drawable.ic_baseline_person_24)
                .apply(RequestOptions().override(100, 100)).into(binding.imgItemPhoto)
            binding.cvItemRoom.setOnClickListener(
                CustomOnItemClickListener(
                    adapterPosition,
                    object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(view: View, position: Int) {
//                    open chat room
//                    val intent = Intent(activity, NoteAddUpdateActivity::class.java)
//                    intent.putExtra(NoteAddUpdateActivity.EXTRA_POSITION, position)
//                    intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, note)
//                    activity.startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_UPDATE)
                        }
                    })
            )
        }
    }
}