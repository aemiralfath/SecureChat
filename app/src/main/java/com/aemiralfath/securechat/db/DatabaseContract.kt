package com.aemiralfath.securechat.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class RoomColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "room"
            const val _ID = "_id"
            const val _ID_SERVER_ROOM = "id server room"
            const val NAME = "title"
            const val LAST_CHAT = "last chat"
            const val LAST_DATE = "last date"
            const val IMAGE = "image"
        }
    }
}