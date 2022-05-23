package com.example.homeanimationpoc

import android.util.Log
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.min

class StackCardLayoutManager() : RecyclerView.LayoutManager() {

    private val mShrinkAmount = 0.15f
    private val mShrinkDistance = 0.9f

    private val addedChildren: List<View>
        get() = (0 until childCount).map {
            getChildAt(it) ?: throw NullPointerException()
        }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams =
        RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT)

    override fun isAutoMeasureEnabled(): Boolean = true

    override fun onLayoutChildren(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ) {
        if (state.getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }

        //onceCompleteScrollLength = -1;
        //分离全部已有的view，放入临时缓存
        detachAndScrapAttachedViews(recycler);

        //fill(recycler, state, 0);
    }

    override fun canScrollVertically(): Boolean = true

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): Int = dy.also { deltaY ->
        if (childCount == 0) {
            return@also
        }

        val scrolled = super.scrollVerticallyBy(dy, recycler, state)

        val midpoint = height / 2f
        val d0 = 0f
        val d1 = mShrinkDistance * midpoint
        val s0 = 1f
        val s1 =  1f - mShrinkAmount

        addedChildren.forEachIndexed { index, view ->
            val initializedTop = view.getTag(InitializedPosition.TOP.key) as Int
            val layoutParams = view.layoutParams as RecyclerView.LayoutParams
            val left = layoutParams.marginStart
            val top = Math.min(Math.max((view.top - deltaY), index * 30), initializedTop)
            val right = view.measuredWidth + layoutParams.marginEnd
            val bottom = top + view.measuredHeight

            layoutDecorated(view, left, top, right, bottom)

            val childMidpoint = (getDecoratedBottom(view) + getDecoratedTop(view)) / 2f
            val d: Float = min(d1, abs(midpoint - childMidpoint))
            val scale: Float = 0.9f

            Log.d("scrollVerticallyBy %d", dy.toString());

            view.scaleX = scale
            view.scaleY = scale
        }

        return scrolled
    }

    private enum class InitializedPosition(val key: Int) {
        TOP(R.integer.top)
    }
}