package com.example.mediaplayer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.R
import com.example.mediaplayer.model.album_model

class adapteralbum(var mlistAlbum: MutableList<album_model>) :
    RecyclerView.Adapter<adapteralbum.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapteralbum.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.itemlist_album, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mlistAlbum.size
    }

    override fun onBindViewHolder(holder: adapteralbum.ViewHolder, position: Int) {
        val malbum: album_model = mlistAlbum[position]
        holder.namealbum.text = malbum.name
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var namealbum = itemView.findViewById(R.id.title_album) as TextView
    }
}