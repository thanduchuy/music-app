package com.example.mediaplayer.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.*
import com.example.mediaplayer.dbhelper.albumdbhelper
import com.example.mediaplayer.model.album_model
import com.example.mediaplayer.model.music_model


class adaptersong(var mlistSong: MutableList<music_model>, val context: Context) :
    RecyclerView.Adapter<adaptersong.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adaptersong.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_listmusic, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(mlistSong.size>6)
        {
            return 6
        }
        else
        {
            return mlistSong.size
        }
    }

    override fun onBindViewHolder(holder: adaptersong.ViewHolder, position: Int) {
        val msong: music_model = mlistSong[position]
        var maxTitle = msong.name.length
        var maxAuthorName = msong.author.length
        if (maxTitle > 20) maxTitle = 20
        if (maxAuthorName > 20) maxAuthorName = 20
        holder.namesong.text=msong.name.substring(0, maxTitle)
        holder.Athornamesong.text=msong.author.substring(0, maxAuthorName)
        holder.play.setOnClickListener {
            try {
                Service.startPlay(position)
                var intent = Intent(context, PlayerProcessing::class.java)
                val bundle = Bundle()
                bundle.putString("mTitle", msong.name)
                bundle.putString("mAuthorName", msong.author)
                bundle.putString("mSongURL", msong.url)
                bundle.putString("mSize", msong.duration.toString())
                intent.putExtras(bundle)
                context.startActivity(intent)
            } catch (ex: java.lang.Exception) {
                Log.e("Player", ex.toString())
            }
        }
        holder.add.setOnClickListener {
            val album= albumdbhelper(context)
            val mlistAlbum = album.readAllAlbum()
            var song = SongInfo(msong.name, msong.author, msong.url, 2, 1 )
            showNoticeDialog(mlistAlbum, song)
            album.close()
        }
    }

//    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
//        super.onAttachedToRecyclerView(recyclerView)
//        this.context = recyclerView.context
//    }
    fun showNoticeDialog(dataAlbum: MutableList<album_model>, song: SongInfo) {
        // Create an instance of the dialog fragment and show it
        val dialog = AddMusicToAlbumDialog(dataAlbum, context as Activity, song)
        dialog.show()
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var namesong = itemView.findViewById(R.id.tvSongName) as TextView
        var Athornamesong=itemView.findViewById(R.id.tvAuthor)as TextView
        var play: ImageView = itemView.findViewById(R.id.buttonPlay)
        var add: ImageView = itemView.findViewById(R.id.buttonAdd)
    }
}