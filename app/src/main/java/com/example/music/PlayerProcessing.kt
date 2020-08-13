package com.example.mediaplayer

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mediaplayer.DBHandler.DatabaseHandlerMusic
import com.example.mediaplayer.model.music_model
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_player_processing.*
import java.lang.reflect.Type


class PlayerProcessing : AppCompatActivity() {
    var title = ""
    var author = ""
    var size = ""
    var url = ""
    var listLike = ArrayList<music_model>()
    override fun onCreate(savedInstanceState: Bundle?) {
        var isPause: Boolean = false
        var isStop: Boolean = false
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_processing)
        setSupportActionBar(nav_bar_default as androidx.appcompat.widget.Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val intent = intent
        val bundle = intent.extras
        var music: music_model? = null
        val sharedPref: SharedPreferences = getSharedPreferences("list", 0)
        if(sharedPref.contains("list")){
            listLike = getArrayList("list")
        }
        if (bundle !== null){
            title = bundle.getString("mTitle").toString()
            author = bundle.getString("mAuthorName").toString()
            size = bundle.getString("mSize").toString()
            url = bundle.getString("mSongURL").toString()
            tvSongName.text = title
            tvAuthor.text = author
            sbProgressPlayer.max = size!!.toInt()
            textviewNumber2.text = convertMilliseconds(size.toLong())
            buttonPause.setImageResource(R.drawable.ic_pause_circle)
            val db = DatabaseHandlerMusic(this)
            music = db.getSongByUrl(Service.listSongs[Service.currentPosition].mSongURL!!)
            if (checkMusic(title)) {
                Favorite.setImageResource(R.drawable.ic_favorite_24dp)
            } else {
                Favorite.setImageResource(R.drawable.ic_favorite_border)
            }
            db.close()

        }
        buttonStop.setOnClickListener {
            if (!isStop) {
                Service.stopPlay()
                isStop = true
                isPause = true
                buttonPause.setImageResource(R.drawable.ic_playcircle)
                sbProgressPlayer.progress = 0
                textviewNumber1.text = convertMilliseconds(0)
            }
        }
        buttonPause.setOnClickListener {
            if (!isStop) {
                if (!isPause) {
                    Service.pausePlay()
                    isPause = true
                    buttonPause.setImageResource(R.drawable.ic_playcircle)
                } else {
                    Service.resumePlay()
                    isPause = false
                    buttonPause.setImageResource(R.drawable.ic_pause_circle)
                }
            } else {
                Service.startPlay(Service.currentPosition)
                isStop = false
                isPause = false
                buttonPause.setImageResource(R.drawable.ic_pause_circle)
            }
        }
        buttonNext.setOnClickListener {
            Service.nextSong()
            isStop = false
            isPause = false
            buttonPause.setImageResource(R.drawable.ic_pause_circle)
            tvSongName.text = Service.listSongs[Service.currentPosition].mTitle
            tvAuthor.text = Service.listSongs[Service.currentPosition].mAuthorName
            sbProgressPlayer.max = Service.listSongs[Service.currentPosition].mSize
            val db = DatabaseHandlerMusic(this)
            music = db.getSongByUrl(Service.listSongs[Service.currentPosition].mSongURL!!)
            if (checkMusic(Service.listSongs[Service.currentPosition].mTitle.toString())) {
                Favorite.setImageResource(R.drawable.ic_favorite_24dp)
            } else {
                Favorite.setImageResource(R.drawable.ic_favorite_border)
            }
            db.close()
        }
        buttonPre.setOnClickListener {
            Service.preSong()
            isStop = false
            isPause = false
            buttonPause.setImageResource(R.drawable.ic_pause_circle)
            tvSongName.text = Service.listSongs[Service.currentPosition].mTitle
            tvAuthor.text = Service.listSongs[Service.currentPosition].mAuthorName
            sbProgressPlayer.max = Service.listSongs[Service.currentPosition].mSize
        }
        sbProgressPlayer?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
                Service.seekToProgress(seek.progress)
            }
        })
        Favorite.setOnClickListener {

            if (checkMusic(title)) {
                Favorite.setImageResource(R.drawable.ic_favorite_border)
                listLike = removeMusicFavourite(title)
                saveArrayList(listLike,"list")
            } else {
                Favorite.setImageResource(R.drawable.ic_favorite_24dp)
                listLike.add(music_model(1,url,title,author,size.toInt(),true))
                saveArrayList(listLike,"list")
            }
        }
        var myTracking = MySongTrack()
        myTracking.start()
    }
    fun checkMusic(name:String) : Boolean {
        listLike.forEach {
            if (it.name == name) {
                return true
            }
        }
        return false
    }
    fun removeMusicFavourite(name:String) : ArrayList<music_model> {
        var temp = listLike
        temp = ArrayList(temp.filter { s -> s.name != name })
        return temp
    }
    fun getArrayList(key: String?): ArrayList<music_model> {
        val sharedPref: SharedPreferences = getSharedPreferences("list", 0)
        val gson = Gson()
        val json: String? = sharedPref.getString(key, null)
        val type: Type = object : TypeToken<ArrayList<music_model>>() {}.getType()
        return gson.fromJson(json, type)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun saveArrayList(list: ArrayList<music_model>, key: String?) {
        val sharedPref: SharedPreferences = getSharedPreferences("list", 0)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        val gson = Gson()
        val json: String = gson.toJson(list)
        editor.putString(key, json)
        editor.apply()
    }
    fun convertMilliseconds(milliseconds: Long): String {
        val minutes = milliseconds / 1000 / 60
        val seconds = milliseconds / 1000 % 60
        return "${minutes}:${seconds}"
    }
    inner class MySongTrack(): Thread() {

        override fun run() {
            while (true) {
                try {
                    Thread.sleep(1000)
                } catch (ex: Exception) {

                }
                runOnUiThread {
                    if (!Service.isStop) {
                        Service.setSbProgress(Service.mp.currentPosition)
                    }
                    val progress = Service.progress
                    sbProgressPlayer.progress = progress
                    textviewNumber1.text = convertMilliseconds(progress.toLong())
                }
            }
        }
    }
}