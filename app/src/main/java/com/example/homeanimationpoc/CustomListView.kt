package com.example.homeanimationpoc

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.appbar.AppBarLayout

class CustomListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = DEFAULT_STYLE_ATTRIBUTE
) : AppBarLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.custom_list, this)
    }

    companion object {
        private const val DEFAULT_STYLE_ATTRIBUTE = 0
    }
}