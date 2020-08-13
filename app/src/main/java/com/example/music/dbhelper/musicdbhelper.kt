package com.example.mediaplayer.dbhelper


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.mediaplayer.model.music_model

class musicdbhelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "MediaPlayer"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MusicContract.MusicEntry.TABLE_NAME + " (" +
                    MusicContract.MusicEntry.COLUMN_ID_MUSIC + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MusicContract.MusicEntry.COLUMN_URL_MUSIC + " TEXT," +
                    MusicContract.MusicEntry.COLUMN_NAME_MUSIC + " TEXT," +
                    MusicContract.MusicEntry.COLUMN_AUTHOR_MUSIC + " TEXT," +
                    MusicContract.MusicEntry.COLUMN_DURATION_MUSIC + " INTEGER," +
                    MusicContract.MusicEntry.COLUMN_ISLIKE_MUSIC + " INTEGER)"
        private val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MusicContract.MusicEntry.TABLE_NAME
    }

    override fun onCreate(p0: SQLiteDatabase) {
        p0.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        p0.execSQL(SQL_DELETE_ENTRIES)
        onCreate(p0)
    }

    fun insertMusic(music: music_model) {
        // Gets the data repository in write mode
        val db = this.writableDatabase
        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(MusicContract.MusicEntry.COLUMN_URL_MUSIC, music.url)
        values.put(MusicContract.MusicEntry.COLUMN_NAME_MUSIC, music.name)
        values.put(MusicContract.MusicEntry.COLUMN_AUTHOR_MUSIC, music.author)
        values.put(MusicContract.MusicEntry.COLUMN_DURATION_MUSIC, music.duration)
        values.put(MusicContract.MusicEntry.COLUMN_ISLIKE_MUSIC, music.isLike)

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(MusicContract.MusicEntry.TABLE_NAME, null, values)
    }

    fun readAllMusic(): MutableList<music_model> {
        val music_model = mutableListOf<music_model>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + MusicContract.MusicEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return mutableListOf()
        }

        var music_id: Int
        var music_url: String
        var music_name: String
        var music_author: String
        var music_duration: Int
        var music_islike: Int
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                music_id =
                    cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_ID_MUSIC))
                music_url =
                    cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_URL_MUSIC))
                music_name =
                    cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_NAME_MUSIC))
                music_author =
                    cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_AUTHOR_MUSIC))
                music_duration =
                    cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_DURATION_MUSIC))
                music_islike =
                    cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_ISLIKE_MUSIC))
                music_model.add(
                    music_model(
                        music_id,
                        music_url,
                        music_name,
                        music_author,
                        music_duration,
                        music_islike > 0
                    )
                )
                cursor.moveToNext()
            }
        }
        return music_model
    }
}