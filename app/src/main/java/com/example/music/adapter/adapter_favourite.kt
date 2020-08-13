package com.example.music.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.PlayerProcessing
import com.example.mediaplayer.R
import com.example.mediaplayer.model.music_model
import kotlinx.android.synthetic.main.item_favourite.view.*


class AdapterFavourite(val context: Context, var likes: ArrayList<music_model>) :
    RecyclerView.Adapter<AdapterFavourite.myViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_favourite, parent, false)
        return myViewHolder(view)
    }

    override fun getItemCount(): Int {
        return likes.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val hobby = likes[position]
        holder.setData(hobby!!, position)
        holder.itemView.alpha = 0.toFloat()
        holder.itemView.translationY = 200.toFloat()

        holder.itemView.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(800)
            .setStartDelay((position*100).toLong()).start()
        val msong: music_model = likes[position]
        var maxTitle = msong.name.length
        var maxAuthorName = msong.author.length
        if (maxTitle > 20) maxTitle = 20
        if (maxAuthorName > 20) maxAuthorName = 20
        holder.itemView.title.text=msong.name.substring(0, maxTitle)
        holder.itemView.author.text=msong.author.substring(0, maxAuthorName)
        holder.itemView.btnGo.setOnClickListener {
            var intent = Intent(context, PlayerProcessing::class.java)
            val bundle = Bundle()
            bundle.putString("mTitle", msong.name)
            bundle.putString("mAuthorName", msong.author)
            bundle.putString("mSongURL", msong.url)
            bundle.putString("mSize", msong.duration.toString())
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    inner class myViewHolder(myView: View) : RecyclerView.ViewHolder(myView) {
        var currentHobby: music_model? = null
        var currentPosition: Int = 0

        init {
        }

        fun setData(dash: music_model?, position: Int) {
            this.currentHobby = dash
            this.currentPosition = position
        }
    }
}