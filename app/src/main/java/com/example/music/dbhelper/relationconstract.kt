package com.example.mediaplayer.dbhelper

import android.provider.BaseColumns

object RelationContract {

    /* Inner class that defines the table contents */
    class RelationEntry : BaseColumns {
        companion object {
            val TABLE_NAME="relation"
            val COLUMN_ID_Music = "idMusic"
            val COLUMN_ID_Album = "idAlbum"
        }
    }
}