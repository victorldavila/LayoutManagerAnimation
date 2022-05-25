package com.example.homeanimationpoc

import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation

class TransformScaleAnimation(
    private val startScaleXValue: Float,
    private val endScaleXValue: Float,
    private val startScaleYValue: Float,
    private val endScaleYValue: Float,
    duration: Long,
    startOffSet: Long,
    transformUpListener: Animation.AnimationListener,
    transformDownListener: Animation.AnimationListener
): TransformAnimation {
    private val scaleUp by lazy { ScaleAnimation(startScaleXValue, endScaleXValue, startScaleYValue, endScaleYValue, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f) }
    private val scaleDown by lazy { ScaleAnimation(endScaleXValue, startScaleXValue, endScaleYValue, startScaleYValue, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f) }

    init {
        scaleUp.duration = duration
        scaleUp.startOffset = startOffSet
        scaleUp.fillAfter = true
        scaleUp.isFillEnabled = true
        scaleUp.setAnimationListener(transformUpListener)

        scaleDown.duration = duration
        scaleDown.startOffset = startOffSet
        scaleDown.fillAfter = true
        scaleDown.isFillEnabled = true
        scaleDown.setAnimationListener(transformDownListener)
    }

    override fun transformUp(view: View) {
        view.startAnimation(scaleUp)
    }

    override fun transformDown(view: View) {
        view.startAnimation(scaleDown)
    }

    override fun getUpAnimation() = scaleUp

    override fun getDownAnimation() = scaleDown

    class TransformScaleBuilder: TransformAnimationBuilder() {
        private var startScaleXValue = 0f
        private var endScaleXValue = 1f

        private var startScaleYValue = 0f
        private var endScaleYValue = 1f

        fun setStartScaleXValue(startScaleX: Float): TransformScaleBuilder {
            startScaleXValue = startScaleX

            return this@TransformScaleBuilder
        }

        fun setStartScaleYValue(startScaleY: Float): TransformScaleBuilder {
            startScaleYValue = startScaleY

            return this@TransformScaleBuilder
        }

        fun setEndScaleXValue(endScaleX: Float): TransformScaleBuilder {
            endScaleXValue = endScaleX

            return this@TransformScaleBuilder
        }

        fun setEndScaleYValue(endScaleY: Float): TransformScaleBuilder {
            endScaleYValue = endScaleY

            return this@TransformScaleBuilder
        }

        override fun build(): TransformAnimation =
            TransformScaleAnimation(
                startScaleXValue,
                endScaleXValue,
                startScaleYValue,
                endScaleYValue,
                animateDuration,
                animateStartOffSet,
                getTransformListener(transformUpStartAnimation, transformUpEndAnimation),
                getTransformListener(transformDownStartAnimation, transformDownEndAnimation)
            )
    }
}