package com.example.mediaplayer.dbhelper

import android.provider.BaseColumns

object MusicContract {

    /* Inner class that defines the table contents */
    class MusicEntry : BaseColumns {
        companion object {
            val TABLE_NAME="Music"
            val COLUMN_ID_MUSIC="_id"
            val COLUMN_URL_MUSIC = "url"
            val COLUMN_NAME_MUSIC = "name"
            val COLUMN_AUTHOR_MUSIC = "author"
            val COLUMN_DURATION_MUSIC = "duration"
            val COLUMN_ISLIKE_MUSIC = "isLike"
        }
    }
}