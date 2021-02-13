package com.aemiralfath.securechat.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.aemiralfath.securechat.db.DatabaseContract.RoomColumns
import com.aemiralfath.securechat.db.DatabaseContract.RoomColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "dbchatapp"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_ROOM = "CREATE TABLE $TABLE_NAME" +
                " (${RoomColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${RoomColumns._ID_SERVER_ROOM} TEXT NO NULL," +
                " ${RoomColumns.NAME} TEXT NO NULL," +
                " ${RoomColumns.LAST_CHAT} TEXT NO NULL," +
                " ${RoomColumns.LAST_DATE} TEXT NO NULL," +
                " ${RoomColumns.IMAGE} TEXT NO NULL)"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(SQL_CREATE_TABLE_ROOM)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0)
    }
}