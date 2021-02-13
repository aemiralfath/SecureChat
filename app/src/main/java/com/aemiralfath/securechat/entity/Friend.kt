package com.aemiralfath.securechat.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Friend(
    var id: Int = 0,
    var name: String? = null,
    var status: String? = null,
    var profileImage: String? = null
):Parcelable
