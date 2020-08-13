package com.example.mediaplayer.dbhelper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.mediaplayer.model.album_model

class albumdbhelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "MediaPlayer"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AlbumContract.AlbumEntry.TABLE_NAME + " (" +
                    AlbumContract.AlbumEntry.COLUMN_ID_ALBUM + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    AlbumContract.AlbumEntry.COLUMN_NAME_ALBUM + " TEXT," +
                    AlbumContract.AlbumEntry.COLUMN_ISLIKE_ALBUM + " INTEGER)"

        private val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AlbumContract.AlbumEntry.TABLE_NAME
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(p0)
    }

    fun insertAlbum(name: String, isLike: Boolean) {
        // Gets the data repository in write mode
        val db = this.writableDatabase
        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(AlbumContract.AlbumEntry.COLUMN_NAME_ALBUM, name)
        values.put(AlbumContract.AlbumEntry.COLUMN_ISLIKE_ALBUM, isLike)

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(AlbumContract.AlbumEntry.TABLE_NAME, null, values)
    }

    fun readAllAlbum(): MutableList<album_model> {
        val album_model = mutableListOf<album_model>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + AlbumContract.AlbumEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return mutableListOf()
        }

        var album_id: Int
        var album_name: String
        var album_islike: Int
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                album_id =
                    cursor.getInt(cursor.getColumnIndex(AlbumContract.AlbumEntry.COLUMN_ID_ALBUM))
                album_name =
                    cursor.getString(cursor.getColumnIndex(AlbumContract.AlbumEntry.COLUMN_NAME_ALBUM))
                album_islike =
                    cursor.getInt(cursor.getColumnIndex(AlbumContract.AlbumEntry.COLUMN_ISLIKE_ALBUM))

                album_model.add(album_model(album_id, album_name, album_islike > 0))
                cursor.moveToNext()
            }
        }
        return album_model
    }
}