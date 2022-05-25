package com.example.homeanimationpoc

import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    val list: RecyclerView by lazy { findViewById(R.id.listHome) }
    val fab: FloatingActionButton by lazy { findViewById(R.id.fab) }
    val card1: CardView by lazy { findViewById(R.id.firstCard) }
    val card2: CardView by lazy { findViewById(R.id.secondCard) }
    val card3: CardView by lazy { findViewById(R.id.thirdCard) }
    val card4: CardView by lazy { findViewById(R.id.fourthCard) }

    val widthDp by lazy { resources.configuration.screenWidthDp }
    val heightDp by lazy { resources.configuration.screenHeightDp }

    private var isUpTransform: Boolean = true

    private val transformAlphaAnimation by lazy {
        TransformAlphaAnimation.TransformAlphaBuilder()
            .setFillAfter(true)
            .setTransformDownStartListener { fab.isClickable = false }
            .setTransformUpStartListener { fab.isClickable = true }
            .build()
    }

    private val transformScaleAnimation by lazy {
        TransformScaleAnimation.TransformScaleBuilder()
            .build()
    }

    private val animationSet1 by lazy {
        createAnimation(
            card1.translationX,
            -(screenWidth - card1.measuredWidth + 30).toFloat(),
            card1.translationY,
            -(card1.measuredHeight + 50 + (card1.measuredHeight / 3f))//((card1.height / 2f) + card1.height + 16)
        ) { card1.isVisible = true }
    }

    private val animationSet2 by lazy {
        createAnimation(
            card1.translationX,
            -((screenWidth / 2) - card1.measuredWidth + 10).toFloat(),
            card1.translationY,
            -(card1.measuredHeight + 50 + (card1.measuredHeight / 3f))
        ) { card2.isVisible = true }
    }

    private val animationSet3 by lazy {
        createAnimation(
            card1.translationX,
            -(screenWidth - card1.measuredWidth + 30).toFloat(),
            card1.translationY,
            -(card1.measuredHeight / 3f)
        ) { card3.isVisible = true }
    }

    private val animationSet4 by lazy {
        createAnimation(
            card1.translationX,
            -((screenWidth / 2) - card1.measuredWidth + 10).toFloat(),
            card1.translationY,
            -(card1.measuredHeight / 3f)
        ) { card4.isVisible = true }
    }

    private val screenWidth by lazy {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        metrics.widthPixels
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        card1.setOnClickListener {
            transformAlphaAnimation.transformUp(fab)

            isUpTransform = if (isUpTransform) {
                animationSet1.transformUp(card1)
                animationSet2.transformUp(card2)
                animationSet3.transformUp(card3)
                animationSet4.transformUp(card4)

                transformAlphaAnimation.transformDown(fab)

                false
            } else {
                animationSet1.transformDown(card1)
                animationSet2.transformDown(card2)
                animationSet3.transformDown(card3)
                animationSet4.transformDown(card4)

                transformAlphaAnimation.transformUp(fab)

                true
            }
        }

        fab.setOnClickListener {
            isUpTransform = if (isUpTransform) {
                animationSet1.transformUp(card1)
                animationSet2.transformUp(card2)
                animationSet3.transformUp(card3)
                animationSet4.transformUp(card4)

                transformAlphaAnimation.transformDown(fab)

                false
            } else {
                animationSet1.transformDown(card1)
                animationSet2.transformDown(card2)
                animationSet3.transformDown(card3)
                animationSet4.transformDown(card4)

                transformAlphaAnimation.transformUp(fab)

                true
            }
        }

        val adapter = RecycleViewAnimation()
        list.layoutManager = VegaLayoutManager()
        val itemDecor = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = true.also { _ ->
                    val fromPos = viewHolder.adapterPosition
                    val toPos = target.adapterPosition
                    adapter.notifyItemMoved(fromPos, toPos)
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                }
            })
        itemDecor.attachToRecyclerView(list)
        list.adapter = adapter
    }

    private fun createAnimation(startXValue: Float, endXValue: Float, startYValue: Float, endYValue: Float, startListener: (() -> Unit)? = null): TransformAnimationsSet {
        val translateAnimation = createTranslateAnimation(startXValue, endXValue, startYValue, endYValue, startListener)
        return createAnimationSet(translateAnimation)
    }

    private fun createTranslateAnimation(startXValue: Float, endXValue: Float, startYValue: Float, endYValue: Float, startListener: (() -> Unit)?) =
        TransformTranslateAnimation.TransformTranslateBuilder()
            .setStartTranslateXValue(startXValue)
            .setEndTranslateXValue(endXValue)
            .setStartTranslateYValue(startYValue)
            .setEndTranslateYValue(endYValue)
            .setTransformUpStartListener(startListener)
            .build()

    private fun createAnimationSet(transformAnimation: TransformAnimation) =
        TransformAnimationsSet.TransformsSetBuilder()
            .addUpAnimation(transformAnimation.getUpAnimation())
            .addUpAnimation(transformScaleAnimation.getUpAnimation())
            .addDownAnimation(transformAnimation.getDownAnimation())
            .addDownAnimation(transformScaleAnimation.getDownAnimation())
            .buildSetTransform()
}