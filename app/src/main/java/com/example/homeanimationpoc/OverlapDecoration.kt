package com.example.homeanimationpoc

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class OverlapDecoration : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        val itemPosition = parent.getChildAdapterPosition(view)

        if (itemPosition == 0) {
            return
        }

        outRect.set(0, 0, 0, -150) //<-- bottom
    }
}