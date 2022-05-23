package com.example.homeanimationpoc

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class CustomHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = DEFAULT_STYLE_ATTRIBUTE
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.custom_header, this)
    }

    companion object {
        private const val DEFAULT_STYLE_ATTRIBUTE = 0
    }
}