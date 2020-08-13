package com.example.mediaplayer

import android.content.ContentUris
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.adapter.MyAlbumAdapter
import com.example.mediaplayer.dbhelper.albumdbhelper
import com.example.mediaplayer.model.album_model
import kotlinx.android.synthetic.main.layout_load_all_song.*
import kotlinx.android.synthetic.main.item_listmusic.view.*
import java.io.FileDescriptor


class LoadAllSong : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_load_all_song)
        setSupportActionBar(nav_bar_default as androidx.appcompat.widget.Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        loadSong()
        var myTracking = MySongTrack()
        myTracking.start()
        sbProgress?.setOnSeekBarChangeListener(object :
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
    }

    inner class MySongAdapter(private val myDataset: ArrayList<SongInfo>) :
        RecyclerView.Adapter<MySongAdapter.MyViewHolder>() {

        inner class MyViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
            var tvSongName: TextView = view.tvSongName
            var tvAuthor: TextView = view.tvAuthor
            var play: ImageView = view.buttonPlay
            var image: ImageView = view.imageSong
            var add: ImageView = view.buttonAdd
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MySongAdapter.MyViewHolder {
            val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_listmusic, parent, false) as View
            return MyViewHolder(textView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val song = myDataset[position]
            var maxTitle = 0
            var maxAuthorName = 0
            song.mTitle.let {
                it -> maxTitle = it!!.length
            }
            if (maxTitle > 20) maxTitle = 20
            song.mAuthorName.let {
                    it -> maxAuthorName = it!!.length
            }
            if (maxAuthorName > 20) maxAuthorName = 20
            holder.tvSongName.text = song.mTitle?.substring(0, maxTitle)
            holder.tvAuthor.text =song.mAuthorName?.substring(0, maxAuthorName)
            if (getAlbumart(song.mInageId) != null ){
                holder.image.setImageBitmap(getAlbumart(song.mInageId))
            }
            holder.play.setOnClickListener {
                try {
                    Service.startPlay(position)
                    sbProgress.max = Service.mp.duration
                    var intent = Intent(this@LoadAllSong, PlayerProcessing::class.java)
                    val bundle = Bundle()
                    bundle.putString("mTitle", song.mTitle)
                    bundle.putString("mAuthorName", song.mAuthorName)
                    bundle.putString("mSongURL", song.mSongURL)
                    bundle.putString("mSize", song.mSize.toString())
                    intent.putExtras(bundle)
                    startActivity(intent)
                } catch (ex: java.lang.Exception) {
                    Log.e("Player", ex.toString())
                }
            }
            holder.add.setOnClickListener {
                val album= albumdbhelper(this@LoadAllSong)
                val mlistAlbum = album.readAllAlbum()
                showNoticeDialog(mlistAlbum, song)
                album.close()
            }
        }

        override fun getItemCount() = myDataset.size
    }
    fun showNoticeDialog(dataAlbum: MutableList<album_model>, song: SongInfo) {
        // Create an instance of the dialog fragment and show it
        val dialog = AddMusicToAlbumDialog(dataAlbum, this, song)
        dialog.show()
    }
    inner class MySongTrack(): Thread() {

        override fun run() {
            while (true) {
                try {
                    Thread.sleep(1000)
                } catch (ex: Exception) {

                }
                runOnUiThread {
                    val isStop = Service.isStop
                    if (Service.isStop) {
                        sbProgress.max = 0
                        sbProgress.progress = 0
                        Service.setSbProgress(0)
                    } else {
                        val mp = Service.mp
                        sbProgress.max = mp.duration
                        sbProgress.progress = mp.currentPosition
                        Service.setSbProgress(mp.currentPosition)
                    }
                }
            }
        }
    }
    fun getAlbumart(album_id: Long?): Bitmap? {
        var bm: Bitmap? = null
        try {
            val sArtworkUri: Uri = Uri
                .parse("content://media/external/audio/albumart")
            val uri: Uri = ContentUris.withAppendedId(sArtworkUri, album_id!!)
            val pfd: ParcelFileDescriptor? = contentResolver.openFileDescriptor(uri, "r")
            if (pfd != null) {
                val fd: FileDescriptor = pfd.fileDescriptor
                bm = BitmapFactory.decodeFileDescriptor(fd)
            }
        } catch (e: java.lang.Exception) {
        }
        return bm
    }
    private fun loadSong() {
        val allSongURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC  + "!= 0"
        val cursor = contentResolver.query(allSongURI,null , selection, null, null)
        var list = ArrayList<SongInfo> ()
        if (cursor !== null) {
            if (cursor.moveToFirst()) {
                do {
                    val songURL = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val songAuthor = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val songName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    val time = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val imageId = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
                    val song = SongInfo(songName, songAuthor, songURL, time.toInt(), imageId.toLong())
                    list.add(song)
                } while (cursor.moveToNext())
            }
            cursor.close()
            Service.setSongList(list)
            viewManager = LinearLayoutManager(this)
            viewAdapter = MySongAdapter(list)

            recyclerView = findViewById<RecyclerView>(R.id.listSongView).apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter

            }
        }
    }
}