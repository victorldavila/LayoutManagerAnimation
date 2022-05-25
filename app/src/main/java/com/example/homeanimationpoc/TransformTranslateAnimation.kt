package com.example.homeanimationpoc

import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation

class TransformTranslateAnimation(
    private val startTranslateXValue: Float,
    private val endTranslateXValue: Float,
    private val startTranslateYValue: Float,
    private val endTranslateYValue: Float,
    duration: Long,
    startOffSet: Long,
    transformUpListener: Animation.AnimationListener,
    transformDownListener: Animation.AnimationListener
): TransformAnimation {
    private val scaleUpAnimation by lazy { TranslateAnimation(startTranslateXValue, endTranslateXValue, startTranslateYValue, endTranslateYValue) }
    private val scaleDownAnimation by lazy { TranslateAnimation(endTranslateXValue, startTranslateXValue, endTranslateYValue, startTranslateYValue) }

    init {
        scaleUpAnimation.duration = duration
        scaleUpAnimation.startOffset = startOffSet
        scaleUpAnimation.fillAfter = true
        scaleUpAnimation.isFillEnabled = true
        scaleUpAnimation.setAnimationListener(transformUpListener)

        scaleDownAnimation.duration = duration
        scaleDownAnimation.startOffset = startOffSet
        scaleDownAnimation.fillAfter = true
        scaleUpAnimation.isFillEnabled = true
        scaleDownAnimation.setAnimationListener(transformDownListener)
    }

    override fun transformUp(view: View) {
        view.startAnimation(scaleUpAnimation)
        //view.translationX = 0f
    }

    override fun transformDown(view: View) {
        view.startAnimation(scaleDownAnimation)
    }

    override fun getUpAnimation() = scaleUpAnimation

    override fun getDownAnimation() = scaleDownAnimation

    private fun simulateScroll(view: View) {
        val handler = Handler()
        val runnable: Runnable = object : Runnable {
            override fun run() {
                handler.postDelayed(this, 10)
                view.translationX = 0f
            }
        }
        runnable.run()
    }

//    private fun calculateTranslatePosition(currentXTranslate: Float, currentPercent: Float) {
//        if (currentPercent == )
//        return currentXTranslate - ()
//    }

    class TransformTranslateBuilder: TransformAnimationBuilder() {
        private var startTranslateXValue: Float = 0f
        private var endTranslateXValue: Float = 0f

        private var startTranslateYValue: Float = 0f
        private var endTranslateYValue: Float = 0f

        fun setStartTranslateXValue(startTranslateX: Float): TransformTranslateBuilder {
            startTranslateXValue = startTranslateX

            return this
        }

        fun setStartTranslateYValue(startTranslateY: Float): TransformTranslateBuilder {
            startTranslateYValue = startTranslateY

            return this
        }

        fun setEndTranslateXValue(endTranslateX: Float): TransformTranslateBuilder {
            endTranslateXValue = endTranslateX

            return this
        }

        fun setEndTranslateYValue(endTranslateY: Float): TransformTranslateBuilder {
            endTranslateYValue = endTranslateY

            return this
        }

        override fun build(): TransformAnimation =
            TransformTranslateAnimation(
                startTranslateXValue,
                endTranslateXValue,
                startTranslateYValue,
                endTranslateYValue,
                animateDuration,
                animateStartOffSet,
                getTransformListener(transformUpStartAnimation, transformUpEndAnimation),
                getTransformListener(transformDownStartAnimation, transformDownEndAnimation)
            )
    }
}