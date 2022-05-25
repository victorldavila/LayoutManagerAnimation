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
            .build()
    }

    private val transformScaleAnimation by lazy {
        TransformScaleAnimation.TransformScaleBuilder()
            .build()
    }

    private val transformTranslateRightDownAnimation by lazy {
        TransformTranslateAnimation.TransformTranslateBuilder()
            .setStartTranslateXValue(fab.translationX)
            .setEndTranslateXValue(-(card1.width / 2f))
            .setStartTranslateYValue(fab.translationY)
            .setEndTranslateYValue(-(card1.height / 2f))
            //.setTransformUpStartListener { card4.isVisible = true }
            .build()
    }

    private val transformTranslateRightUpAnimation by lazy {
        TransformTranslateAnimation.TransformTranslateBuilder()
            .setStartTranslateXValue(fab.translationX)
            .setEndTranslateXValue(-(card1.width / 2f))
            .setStartTranslateYValue(fab.translationY)
            .setEndTranslateYValue(-((card1.height / 2f) + card1.height + 16))
            //.setTransformUpStartListener { card2.isVisible = true }
            .build()
    }

    private val transformTranslateLeftDownAnimation by lazy {
        TransformTranslateAnimation.TransformTranslateBuilder()
            .setStartTranslateXValue(fab.translationX)
            .setEndTranslateXValue(-(screenWidth - card1.width) + 16f)
            .setStartTranslateYValue(fab.translationY)
            .setEndTranslateYValue(-(card1.height / 2f))
            //.setTransformUpStartListener { card3.isVisible = true }
            .build()
    }

    private val transformTranslateLeftUpAnimation by lazy {
        TransformTranslateAnimation.TransformTranslateBuilder()
            .setStartTranslateXValue(fab.translationX)
            .setEndTranslateXValue(-(screenWidth - card1.width) + 16f)
            .setStartTranslateYValue(fab.translationY)
            .setEndTranslateYValue(-((card1.height / 2f) + card1.height + 16))
            //.setTransformUpStartListener { card1.isVisible = true }
            .build()
    }

    private val transformRightUpSet by lazy {
        TransformAnimationsSet.TransformsSetBuilder()
            .addUpAnimation(transformScaleAnimation.getUpAnimation())
            .addUpAnimation(transformTranslateRightUpAnimation.getUpAnimation())
            .addDownAnimation(transformScaleAnimation.getDownAnimation())
            .addDownAnimation(transformTranslateRightUpAnimation.getDownAnimation())
            .buildSetTransform()
    }
    private val transformRightDownSet by lazy {
        TransformAnimationsSet.TransformsSetBuilder()
            .addUpAnimation(transformScaleAnimation.getUpAnimation())
            .addUpAnimation(transformTranslateRightDownAnimation.getUpAnimation())
            .addDownAnimation(transformScaleAnimation.getDownAnimation())
            .addDownAnimation(transformTranslateRightDownAnimation.getDownAnimation())
            .buildSetTransform()
    }

    private val transformLeftUpSet by lazy {
        TransformAnimationsSet.TransformsSetBuilder()
            .addUpAnimation(transformTranslateLeftUpAnimation.getUpAnimation())
            .addUpAnimation(transformScaleAnimation.getUpAnimation())
            .addDownAnimation(transformTranslateLeftUpAnimation.getDownAnimation())
            .addDownAnimation(transformScaleAnimation.getDownAnimation())
            .buildSetTransform()
    }

    private val transformLeftDownSet by lazy {
        TransformAnimationsSet.TransformsSetBuilder()
            .addUpAnimation(transformTranslateLeftDownAnimation.getUpAnimation())
            .addUpAnimation(transformScaleAnimation.getUpAnimation())
            .addDownAnimation(transformTranslateLeftDownAnimation.getDownAnimation())
            .addDownAnimation(transformScaleAnimation.getDownAnimation())
            .buildSetTransform()
    }

    private val screenWidth by lazy {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        metrics.widthPixels
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val configuration = resources.configuration
        val screenWidthDp = configuration.screenWidthDp
        val screenHeightDp = configuration.screenHeightDp

        card1.measuredWidth

        val animate = TransformTranslateAnimation.TransformTranslateBuilder()
            .setStartTranslateXValue(card1.translationX)
            .setEndTranslateXValue(-(screenWidth - card1.measuredWidth).toFloat())
            .setStartTranslateYValue(card1.translationY)
            .setEndTranslateYValue(-((card1.height / 2f) + card1.height + 16))
            .setTransformUpStartListener {
                card1.isVisible = true
            }
            .build()

        val scale = TransformScaleAnimation.TransformScaleBuilder()
            //.setTransformUpStartListener { card1.isVisible = true }
            .build()

        card1.setOnClickListener {
            transformAlphaAnimation.transformDown(fab)
        }

        card2.setOnClickListener {
            transformAlphaAnimation.transformUp(fab)
        }

        fab.setOnClickListener {
            val animateTranslate1 = TransformTranslateAnimation.TransformTranslateBuilder()
                .setStartTranslateXValue(card1.translationX)
                .setEndTranslateXValue(-(screenWidth - card1.measuredWidth + 30).toFloat())
                .setStartTranslateYValue(card1.translationY)
                .setEndTranslateYValue(-(card1.measuredHeight + 50 + (card1.measuredHeight / 3f)))
                .setTransformUpStartListener {
                    card1.isVisible = true
                }
                .build()

            val animateTranslate2 = TransformTranslateAnimation.TransformTranslateBuilder()
                .setStartTranslateXValue(card1.translationX)
                .setEndTranslateXValue(-((screenWidth / 2) - card1.measuredWidth + 10).toFloat())
                .setStartTranslateYValue(card1.translationY)
                .setEndTranslateYValue(-(card1.measuredHeight + 50 + (card1.measuredHeight / 3f)))
                .setTransformUpStartListener {
                    card2.isVisible = true
                }
                .build()

            val animateTranslate3 = TransformTranslateAnimation.TransformTranslateBuilder()
                .setStartTranslateXValue(card1.translationX)
                .setEndTranslateXValue(-(screenWidth - card1.measuredWidth + 30).toFloat())
                .setStartTranslateYValue(card1.translationY)
                .setEndTranslateYValue(-(card1.measuredHeight / 3f))
                .setTransformUpStartListener {
                    card3.isVisible = true
                }
                .build()

            val animateTranslate4 = TransformTranslateAnimation.TransformTranslateBuilder()
                .setStartTranslateXValue(card1.translationX)
                .setEndTranslateXValue(-((screenWidth / 2) - card1.measuredWidth + 10).toFloat())
                .setStartTranslateYValue(card1.translationY)
                .setEndTranslateYValue(-(card1.measuredHeight / 3f))
                .setTransformUpStartListener {
                    card4.isVisible = true
                }
                .build()

            val animate1 = TransformAnimationsSet.TransformsSetBuilder()
                .addUpAnimation(animateTranslate1.getUpAnimation())
                .addUpAnimation(scale.getUpAnimation())
                .addDownAnimation(animateTranslate1.getDownAnimation())
                .addDownAnimation(scale.getDownAnimation())
                .buildSetTransform()

            val animate2 = TransformAnimationsSet.TransformsSetBuilder()
                .addUpAnimation(animateTranslate2.getUpAnimation())
                .addUpAnimation(scale.getUpAnimation())
                .addDownAnimation(animateTranslate2.getDownAnimation())
                .addDownAnimation(scale.getDownAnimation())
                .buildSetTransform()

            val animate3 = TransformAnimationsSet.TransformsSetBuilder()
                .addUpAnimation(animateTranslate3.getUpAnimation())
                .addUpAnimation(scale.getUpAnimation())
                .addDownAnimation(animateTranslate3.getDownAnimation())
                .addDownAnimation(scale.getDownAnimation())
                .buildSetTransform()

            val animate4 = TransformAnimationsSet.TransformsSetBuilder()
                .addUpAnimation(animateTranslate4.getUpAnimation())
                .addUpAnimation(scale.getUpAnimation())
                .addDownAnimation(animateTranslate4.getDownAnimation())
                .addDownAnimation(scale.getDownAnimation())
                .buildSetTransform()

            isUpTransform = if (isUpTransform) {
                animate1.transformUp(card1)
                animate2.transformUp(card2)
                animate3.transformUp(card3)
                animate4.transformUp(card4)

                false
            } else {
                animate1.transformDown(card1)
                animate2.transformDown(card2)
                animate3.transformDown(card3)
                animate4.transformDown(card4)

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
}