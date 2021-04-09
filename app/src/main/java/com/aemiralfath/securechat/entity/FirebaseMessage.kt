package com.aemiralfath.securechat.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FirebaseMessage(
    var text: String? = null,
    var textLength: Int? = null,
    var processingTime: Long? = null,
    var sign: String? = null,
    var publicKey: String? = null,
    var name: String? = null,
    var photoUrl: String? = null,
    var imageUrl: String? = null,
    var decrypt: Boolean? = null,
    var dateTime: String? = null,
): Parcelable
