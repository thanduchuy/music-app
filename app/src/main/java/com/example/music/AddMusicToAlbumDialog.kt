package com.example.mediaplayer

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.adapter.MyAlbumAdapter
import com.example.mediaplayer.model.album_model
import kotlinx.android.synthetic.main.dialog_add_music_to_album.*


class AddMusicToAlbumDialog(
    private val dataAlbum: MutableList<album_model>,
    var activity: Activity,
    val song: SongInfo
) : Dialog(activity),
    View.OnClickListener {
        internal var recyclerView: RecyclerView? = null
        private var mLayoutManager: RecyclerView.LayoutManager? = null


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_add_music_to_album)

            mLayoutManager = LinearLayoutManager(activity)
            val mAdapter = MyAlbumAdapter(dataAlbum, song)
            recyclerView = rv_album_add.apply {
                setHasFixedSize(true)
                layoutManager = mLayoutManager
                adapter = mAdapter

            }

            btn_dismiss.setOnClickListener(this)
            btn_done.setOnClickListener(this)

        }


        override fun onClick(v: View) {
            dismiss()
        }
}