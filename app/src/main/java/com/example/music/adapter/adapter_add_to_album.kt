package com.example.mediaplayer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.R
import com.example.mediaplayer.SongInfo
import com.example.mediaplayer.dbhelper.relationdbhelper
import com.example.mediaplayer.model.album_model
import kotlinx.android.synthetic.main.item_list_album_for_add.view.*
import java.lang.Exception

class MyAlbumAdapter(private val myDataset: MutableList<album_model>, val song: SongInfo) :
    RecyclerView.Adapter<MyAlbumAdapter.MyViewHolder>() {
    private lateinit var context: Context
    inner class MyViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        var tvMusicName: TextView = view.tvAlbumName
        var add: ImageView = view.buttonAdd
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyAlbumAdapter.MyViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_album_for_add, parent, false) as View
        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val album = myDataset[position]
        holder.tvMusicName.text = album.name
        holder.add.setOnClickListener {
            try {
                val db = relationdbhelper(context)
                db.addRelation(album.id, song.mSongURL!!)
                val toast = Toast.makeText(context, "Thành công", Toast.LENGTH_LONG)
                toast.show()
                db.close()
            } catch (e: Exception) {
                val toast = Toast.makeText(context, "Lỗi!", Toast.LENGTH_LONG)
                toast.show()
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.context = recyclerView.context
    }
    override fun getItemCount() = myDataset.size
}