package com.example.homeanimationpoc

import android.graphics.Rect
import android.util.Log
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.view.View
import android.view.ViewGroup
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import kotlin.math.min


/**
 * Created by xmuSistone on 2017/9/20.
 */
class VegaLayoutManager : RecyclerView.LayoutManager() {
    private var lastTopDistance: Int = 0
    private var scroll = 0
    private val locationRects = SparseArray<Rect>()
    private val attachedItems = SparseBooleanArray()
    private val viewTypeHeightMap: ArrayMap<Int, Int> = ArrayMap()
    private var needSnap = false
    private var lastDy = 0
    private var lastTranslationY = 0f
    private var maxTransitionY = 0f
    private var maxScroll = -1
    private var adapter: RecyclerView.Adapter<*>? = null
    private var recycler: Recycler? = null

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onAdapterChanged(
        oldAdapter: RecyclerView.Adapter<*>?,
        newAdapter: RecyclerView.Adapter<*>?
    ) {
        super.onAdapterChanged(oldAdapter, newAdapter)
        adapter = newAdapter
    }

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        this.recycler = recycler

        if (state.isPreLayout) {
            return
        }

        buildLocationRects()

        //layout
        detachAndScrapAttachedViews(recycler)
        layoutItemsOnCreate(recycler)
    }

    private fun buildLocationRects() {
        locationRects.clear()
        attachedItems.clear()

        var tempPosition = paddingTop
        val itemCount = itemCount

        for (i in 0 until itemCount) {
            //1.itemWidth/itemHeight
            val viewType = adapter?.getItemViewType(i)
            var itemHeight: Int

            if (viewTypeHeightMap.containsKey(viewType)) {
                itemHeight = viewTypeHeightMap[viewType]!!
            } else {
                itemHeight = getItemHeight(i)
                viewTypeHeightMap[viewType] = itemHeight
            }

            saveRect(tempPosition, itemHeight, i)

            tempPosition += itemHeight
        }

        computeMaxScroll()
    }

    private fun saveRect(tempPosition: Int, itemHeight: Int, position: Int) {
        val rect = Rect()

        rect.left = paddingLeft
        rect.top = tempPosition
        rect.right = width - paddingRight
        rect.bottom = rect.top + itemHeight

        locationRects.put(position, rect)
        attachedItems.put(position, false)
    }

    private fun getItemHeight(position: Int): Int {
        var itemHeight = 0

        recycler?.getViewForPosition(position)?.let {
            addView(it)
            measureChildWithMargins(
                it,
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
            )

            itemHeight = getDecoratedMeasuredHeight(it)
        }

        return itemHeight
    }

    /**
     * 对外提供接口，找到第一个可视view的index
     */
    fun findFirstVisibleItemPosition(): Int {
        val count = locationRects.size()
        val displayRect = Rect(0, scroll, width, height + scroll)
        for (i in 0 until count) {
            if (Rect.intersects(displayRect, locationRects[i]) &&
                attachedItems[i]
            ) {
                return i
            }
        }
        return 0
    }

    /**
     * 计算可滑动的最大值
     */
    private fun computeMaxScroll() {
        if (itemCount == 0) {
            maxScroll = 0
        } else {
            maxScroll = locationRects[locationRects.size() - 1].bottom - height

            if (maxScroll < 0) {
                maxScroll = 0
                return
            }
            val itemCount = itemCount
            var screenFilledHeight = 0

            for (i in itemCount - 1 downTo 0) {
                val rect = locationRects[i]
                screenFilledHeight += rect.bottom - rect.top
                if (screenFilledHeight > height) {
                    maxScroll += getExtraSnapHeight(rect, screenFilledHeight)
                    break
                }
            }
        }
    }

    private fun getExtraSnapHeight(rect: Rect, screenFilledHeight: Int) = height - (screenFilledHeight - (rect.bottom - rect.top))

    /**
     * 初始化的时候，layout子View
     */
    private fun layoutItemsOnCreate(recycler: Recycler) {
        val itemCount = itemCount
        val displayRect = Rect(0, scroll, width, height + scroll)

        for (i in 0 until itemCount) {
            val thisRect = locationRects[i]
            if (Rect.intersects(displayRect, thisRect)) {
                val childView = recycler.getViewForPosition(i)
                addView(childView)
                measureChildWithMargins(
                    childView,
                    View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED
                )
                layoutItem(childView, locationRects[i])
                attachedItems.put(i, true)
                childView.pivotY = 0f
                childView.pivotX = (childView.measuredWidth / 2).toFloat()
                if (thisRect.top - scroll > height) {
                    break
                }
            }
        }
    }

    /**
     * 初始化的时候，layout子View
     */
    private fun layoutItemsOnScroll() {
        val childCount = childCount
        val itemCount = itemCount
        val displayRect = Rect(0, scroll, width, height + scroll)
        var firstVisiblePosition = -1
        var lastVisiblePosition = -1
        for (i in childCount - 1 downTo 0) {
            val child = getChildAt(i) ?: continue
            val position = getPosition(child)
            if (!Rect.intersects(displayRect, locationRects[position])) {
                removeAndRecycleView(child, recycler!!)
                attachedItems.put(position, false)
            } else {
                lastVisiblePosition = getLastVisibleItem(lastVisiblePosition, position)
                firstVisiblePosition = getFirstVisible(firstVisiblePosition, position)

                layoutItem(child, locationRects[position])
            }
        }

        if (firstVisiblePosition > 0) {
            for (i in firstVisiblePosition - 1 downTo 0) {
                if (Rect.intersects(displayRect, locationRects[i]) &&
                    !attachedItems[i]
                ) {
                    reuseItemOnScroll(i, true)
                } else {
                    break
                }
            }
        }

        for (i in lastVisiblePosition + 1 until itemCount) {
            if (Rect.intersects(displayRect, locationRects[i]) &&
                !attachedItems[i]
            ) {
                reuseItemOnScroll(i, false)
            } else {
                break
            }
        }
    }

    private fun getLastVisibleItem(lastVisiblePosition: Int, position: Int) =
        if (lastVisiblePosition < 0) {
            position
        } else {
            lastVisiblePosition
        }

    private fun getFirstVisible(firstVisiblePosition: Int, position: Int) =
        if (firstVisiblePosition < 0) {
            position
        } else {
            min(firstVisiblePosition, position)
        }

    /**
     * 复用position对应的View
     */
    private fun reuseItemOnScroll(position: Int, addViewFromTop: Boolean) {
        val scrap = recycler!!.getViewForPosition(position)
        measureChildWithMargins(scrap, 0, 0)
        scrap.pivotY = 0f
        scrap.pivotX = (scrap.measuredWidth / 2).toFloat()
        if (addViewFromTop) {
            addView(scrap, 0)
        } else {
            addView(scrap)
        }

        layoutItem(scrap, locationRects[position])
        attachedItems.put(position, true)
    }

    private fun layoutItem(child: View, rect: Rect) {
        Log.d("scroll", scroll.toString())
        val topDistance = scroll - rect.top
        val layoutTop: Int
        val layoutBottom: Int
        val itemHeight = rect.bottom - rect.top
        if (topDistance in 1 until itemHeight) {
            val rate1 = topDistance.toFloat() / itemHeight
            val rate2 = 1 - rate1 * rate1 / 10
            val rate3 = 1f
            child.scaleX = if ((topDistance > ((itemHeight / 4) + (itemHeight / 2))) && maxTransitionY  > child.translationY) child.scaleX else rate2
            child.translationY = getTranslationY(topDistance, itemHeight, child.translationY, rate3)
            lastTopDistance = topDistance

            layoutTop = 0
            layoutBottom = itemHeight

            if (child.translationY > maxTransitionY) {
                maxTransitionY = child.translationY
            }
        } else {
            child.scaleX = 1f
            child.translationY = 0f
            layoutTop = rect.top - scroll
            layoutBottom = rect.bottom - scroll
        }

        layoutDecorated(child, rect.left, layoutTop, rect.right, layoutBottom)
    }

    private fun getTranslationY(topDistance: Int, itemHeight: Int, translationY: Float, rate3: Float): Float {
        return if (topDistance > (6 * (itemHeight / 8))) {
            if (translationY <= 0 && lastTopDistance < topDistance) {
                0f
            } else if (lastTopDistance > topDistance) {
                if (topDistance > (7 * (itemHeight / 8))) {
                    0f
                } else {
                    translationY + rate3 + 2
                }
            } else {
                translationY - rate3 - 2
            }
        } else {
            if (translationY < 0) {
                0f
            } else {
                if (lastTopDistance < topDistance) {
                    translationY + rate3
                } else {
                    translationY - rate3
                }
            }
        }
    }

    override fun canScrollVertically(): Boolean {
        return true
    }

    override fun scrollVerticallyBy(dy: Int, recycler: Recycler, state: RecyclerView.State): Int {
        if (itemCount == 0 || dy == 0) {
            return 0
        }

        var travel = dy
        if (dy + scroll < 0) {
            travel = -scroll
        } else if (dy + scroll > maxScroll) {
            travel = maxScroll - scroll
        }
        scroll += travel
        lastDy = dy

        if (!state.isPreLayout && childCount > 0) {
            layoutItemsOnScroll()
        }

        return travel
    }

    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)
        StartSnapHelper().attachToRecyclerView(view)
    }

    override fun onScrollStateChanged(state: Int) {
        if (state == RecyclerView.SCROLL_STATE_DRAGGING) {
            needSnap = false
        }
        super.onScrollStateChanged(state)
    }

    val snapHeight: Int
        get() {
            if (!needSnap) {
                return 0
            }
            needSnap = false
            val displayRect = Rect(
                0, scroll,
                width, height + scroll
            )
            val itemCount = itemCount
            for (i in 0 until itemCount) {
                val itemRect = locationRects[i]
                if (displayRect.intersect(itemRect)) {
                    if (lastDy > 0) {
                        if (i < itemCount - 1) {
                            val nextRect = locationRects[i + 1]
                            return nextRect.top - displayRect.top
                        }
                    }
                    return itemRect.top - displayRect.top
                }
            }
            return 0
        }

    fun findSnapView(): View? {
        return if (childCount > 0) {
            getChildAt(0)
        } else null
    }

    init {
        isAutoMeasureEnabled = true
    }
}