package com.example.mediaplayer.adapter

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class spacealbum(private val spacing: Int, private val spanCount: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % 3
        outRect.left = column * spacing / spanCount
        outRect.right = spacing - (column + 1) * spacing / spanCount
        if (position >= spanCount) {
            outRect.top = spacing
        }
    }
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        //do nothing
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        //do nothing
    }
}