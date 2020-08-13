package com.example.mediaplayer.dbhelper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.mediaplayer.DBHandler.DatabaseHandlerMusic
import com.example.mediaplayer.model.relation_model

class relationdbhelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "MediaPlayer"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + RelationContract.RelationEntry.TABLE_NAME + " (" +
                    RelationContract.RelationEntry.COLUMN_ID_Music + " INTEGER, " +
                    RelationContract.RelationEntry.COLUMN_ID_Album + " INTEGER, " +
                    "PRIMARY KEY(" +
                    RelationContract.RelationEntry.COLUMN_ID_Music + "," +
                    RelationContract.RelationEntry.COLUMN_ID_Album + "))"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + RelationContract.RelationEntry.TABLE_NAME
    }
    override fun onCreate(p0: SQLiteDatabase) {
        p0.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        p0.execSQL(SQL_DELETE_ENTRIES)
        onCreate(p0)
    }
    fun insertRelation(relation:relation_model){
        // Gets the data repository in write mode
            val db = this.writableDatabase
            // Create a new map of values, where column names are the keys
            val values = ContentValues()
            values.put(RelationContract.RelationEntry.COLUMN_ID_Music, relation.idMusic)
            values.put(RelationContract.RelationEntry.COLUMN_ID_Album, relation.idAlbum)

            // Insert the new row, returning the primary key value of the new row
            val newRowId = db.insert(RelationContract.RelationEntry.TABLE_NAME, null, values)
    }
    fun readAllRelation():MutableList<relation_model> {
        val relation_model = mutableListOf<relation_model>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + RelationContract.RelationEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return mutableListOf()
        }

        var relation_music:Int
        var relation_album:Int
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                relation_music= cursor.getInt(cursor.getColumnIndex(RelationContract.RelationEntry.COLUMN_ID_Music))
                relation_album = cursor.getInt(cursor.getColumnIndex(RelationContract.RelationEntry.COLUMN_ID_Album))

                relation_model.add(relation_model(relation_music,relation_album))
                cursor.moveToNext()
            }
        }
        return relation_model
    }
    fun addRelation(idAlbum: Int, urlMusic: String) {
        val db = writableDatabase
        db.execSQL(SQL_CREATE_ENTRIES)
        var cursor: Cursor? = null
        val query = String.format(
            "SELECT * FROM %s WHERE %s = \"%s\"",
            MusicContract.MusicEntry.TABLE_NAME,
            MusicContract.MusicEntry.COLUMN_URL_MUSIC,
            urlMusic
        )
        try {
            cursor = db.rawQuery(query, null)
            var idMusic: Int
            if (cursor!!.moveToFirst()) {
                idMusic = cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_ID_MUSIC))
                val queryRelation = String.format(
                    "SELECT * FROM %s WHERE %s = \"%s\" AND %s = \"%s\"",
                    RelationContract.RelationEntry.TABLE_NAME,
                    RelationContract.RelationEntry.COLUMN_ID_Album,
                    idAlbum,
                    RelationContract.RelationEntry.COLUMN_ID_Music,
                    idMusic
                )
                val exists = db.rawQuery(queryRelation, null)
                cursor.moveToFirst()
                if (exists.count <= 0) {
                    val values = ContentValues()
                    values.put(RelationContract.RelationEntry.COLUMN_ID_Album, idAlbum)
                    values.put(RelationContract.RelationEntry.COLUMN_ID_Music, idMusic)
                    db.insert(RelationContract.RelationEntry.TABLE_NAME, null, values)
                }
            }
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }
        db.close()
    }
}