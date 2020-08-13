package com.example.mediaplayer.dbhelper

import android.provider.BaseColumns

object AlbumContract {

    /* Inner class that defines the table contents */
    class AlbumEntry : BaseColumns {
        companion object {
            val TABLE_NAME="album"
            val COLUMN_ID_ALBUM="_id"
            val COLUMN_NAME_ALBUM = "name"
            val COLUMN_ISLIKE_ALBUM = "isLike"
        }
    }
}