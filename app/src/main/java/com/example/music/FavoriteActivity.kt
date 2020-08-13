package com.example.mediaplayer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.adapter.adapteralbum
import com.example.mediaplayer.adapter.adaptersong
import com.example.mediaplayer.adapter.spacealbum
import com.example.mediaplayer.dbhelper.albumdbhelper
import com.example.mediaplayer.dbhelper.musicdbhelper
import com.example.mediaplayer.dbhelper.relationdbhelper
import com.example.mediaplayer.model.album_model
import com.example.mediaplayer.model.music_model
import com.example.music.adapter.AdapterFavourite
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.activity_favorite.*
import java.lang.reflect.Type
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoriteActivity : AppCompatActivity() {
    private var mlistSong = mutableListOf<music_model>()//Danh sach nhac co trong he thong
    lateinit var music: musicdbhelper
    var listLike = ArrayList<music_model>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        music= musicdbhelper(this)
        mlistSong=music.readAllMusic()//Danh sach bai nhac co trong database
        countMusic.text = mlistSong.size.toString()
        btnGoBack.setOnClickListener {
            finish()
        }
        val sharedPref: SharedPreferences = getSharedPreferences("list", 0)
        if(sharedPref.contains("list")){
            listLike = getArrayList("list")
            countLike.text = listLike.size.toString()
            setupAdapter(listLike)
        }
    }
    private fun setupAdapter(arr:ArrayList<music_model>) {
        val layoutManager1 = LinearLayoutManager(this)
        layoutManager1.orientation = LinearLayoutManager.VERTICAL
        rv_like.layoutManager = layoutManager1
        val adapter1 = AdapterFavourite(this, arr)
        adapter1.notifyDataSetChanged()

        rv_like.adapter = adapter1
    }
    fun getArrayList(key: String?): ArrayList<music_model> {
        val sharedPref: SharedPreferences = getSharedPreferences("list", 0)
        val gson = Gson()
        val json: String? = sharedPref.getString(key, null)
        val type: Type = object : TypeToken<ArrayList<music_model>>() {}.getType()
        return gson.fromJson(json, type)
    }
}