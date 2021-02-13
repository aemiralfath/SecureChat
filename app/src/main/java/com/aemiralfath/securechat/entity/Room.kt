package com.aemiralfath.securechat.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Room(
    var id: Int = 0,
    var idServerRoom: String? = null,
    var name: String? = null,
    var lastChat: String? = null,
    var lastDate: String? = null,
    var image: String? = null
) : Parcelable
