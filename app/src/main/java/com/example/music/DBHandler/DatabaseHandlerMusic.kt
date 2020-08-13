package com.example.mediaplayer.DBHandler

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.mediaplayer.Service
import com.example.mediaplayer.SongInfo
import com.example.mediaplayer.dbhelper.relationdbhelper
import com.example.mediaplayer.model.music_model
import java.lang.Exception


public class DatabaseHandlerMusic(context: Context?) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {
    val create_music_table = String.format(
        "CREATE TABLE %s(" +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT NOT NULL, " +
                "%s TEXT NOT NULL, " +
                "%s TEXT," +
                "%s INT8 NOT NULL," +
                "%s BOOLEAN" +
                ")",
        TABLE_NAME,
        KEY_ID,
        KEY_URL,
        KEY_NAME,
        KEY_AUTHOR,
        KEY_DURATION,
        KEY_IS_LIKE
    )
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(create_music_table)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        val drop_students_table =
            String.format("DROP TABLE IF EXISTS %s", TABLE_NAME)
        db.execSQL(drop_students_table)
        onCreate(db)
    }

    fun addSong(song: SongInfo) {
        val query = String.format(
            "SELECT * FROM %s WHERE %s = \"%s\"",
            TABLE_NAME,
            KEY_URL,
            song.mSongURL
        )
        val db = this.writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(query, null)
        } catch (e: Exception) {
            db.execSQL(create_music_table)
        }
        if (cursor !== null) {
            cursor.moveToFirst()
            if (cursor.count == 0) {
                val values = ContentValues()
                values.put(KEY_URL, song.mSongURL)
                values.put(KEY_NAME, song.mTitle)
                values.put(KEY_AUTHOR, song.mAuthorName)
                values.put(KEY_DURATION, song.mSize)
                values.put(KEY_IS_LIKE, false)
                db.insert(TABLE_NAME, null, values)
            }
        }
        db.close()
    }
    fun getSongByUrl(url: String): music_model? {
        val query = String.format(
            "SELECT * FROM %s WHERE %s = \"%s\"",
            TABLE_NAME,
            KEY_URL,
            url
        )

        val dbread = this.readableDatabase
        val cursor: Cursor = dbread.rawQuery(query, null)
        var music: music_model? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
            val url = cursor.getString(cursor.getColumnIndex(KEY_URL))
            val name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
            val author = cursor.getString(cursor.getColumnIndex(KEY_AUTHOR))
            val duration = cursor.getInt(cursor.getColumnIndex(KEY_DURATION))
            val isLike = cursor.getInt(cursor.getColumnIndex(KEY_IS_LIKE)) > 0
            music = music_model(id, url, name, author, duration, isLike)
        }

        dbread.close()
        return music
    }

    fun likeOrUnlikeSongByUrl(url: String) {
        val query = String.format(
            "SELECT * FROM %s WHERE %s = \"%s\"",
            TABLE_NAME,
            KEY_URL,
            url
        )
        val db = this.writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(query, null)
        } catch (e: Exception) {
            db.execSQL(create_music_table)
        }
        if (cursor !== null) {
           if (cursor.moveToFirst()) {
               val values = ContentValues()
               val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
               val isLike = cursor.getInt(cursor.getColumnIndex(KEY_IS_LIKE)) > 0
               values.put(KEY_IS_LIKE, !isLike)
               val where = "$KEY_ID=?"
               val whereArgs =
                   arrayOf(java.lang.String.valueOf(id))
               db.update(TABLE_NAME, values, where, whereArgs)
           }
        } else {
            val values = ContentValues()
            val song = Service.listSongs[Service.currentPosition]
            values.put(KEY_URL, song.mSongURL)
            values.put(KEY_NAME, song.mTitle)
            values.put(KEY_AUTHOR, song.mAuthorName)
            values.put(KEY_DURATION, song.mSize)
            values.put(KEY_IS_LIKE, true)
            db.insert(TABLE_NAME, null, values)
        }
        db.close()
    }

    companion object {
        private const val DATABASE_NAME = "MediaPlayer"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Music"
        private const val KEY_ID = "_id"
        private const val KEY_URL = "url"
        private const val KEY_NAME = "name"
        private const val KEY_AUTHOR = "author"
        private const val KEY_DURATION = "duration"
        private const val KEY_IS_LIKE = "isLike"
    }
}
