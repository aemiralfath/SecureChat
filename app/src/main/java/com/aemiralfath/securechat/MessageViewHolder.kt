package com.aemiralfath.securechat

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aemiralfath.securechat.databinding.ItemMessageBinding
import com.aemiralfath.securechat.entity.FirebaseMessage
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val TAG = "MessageViewHolder"
    }

    private var binding = ItemMessageBinding.bind(itemView)

    fun bindMessage(message: FirebaseMessage) {
        when {
            !message.text.isNullOrBlank() -> {
                binding.tvMessage.text = message.text
                binding.tvMessage.visibility = TextView.VISIBLE
                binding.imgMessage.visibility = ImageView.GONE
            }
            !message.imageUrl.isNullOrBlank() -> {
                if (message.imageUrl!!.startsWith("gs://")) {
                    val storageReference =
                        FirebaseStorage.getInstance().getReferenceFromUrl(message.imageUrl!!)

                    storageReference.downloadUrl.addOnSuccessListener {
                        Glide.with(itemView).load(it).into(binding.imgMessage)
                    }.addOnFailureListener {
                        Log.w(TAG, it.message.toString(), it)
                    }
                } else {
                    Glide.with(itemView).load(message.imageUrl).into(binding.imgMessage)
                }

                binding.imgMessage.visibility = ImageView.VISIBLE
                binding.tvMessage.visibility = TextView.GONE

            }
        }

        binding.tvUser.text = message.name ?: "Anonymous"
        Glide.with(itemView).load(message.photoUrl).placeholder(R.drawable.ic_baseline_person_24)
            .into(binding.imgUser)
    }

}