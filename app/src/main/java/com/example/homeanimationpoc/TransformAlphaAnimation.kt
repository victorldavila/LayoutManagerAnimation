package com.example.homeanimationpoc

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

class TransformAlphaAnimation(
    private val startAlphaValue: Float,
    private val endAlphaValue: Float,
    duration: Long,
    startOffSet: Long,
    fillAfter: Boolean,
    transformUpListener: Animation.AnimationListener?,
    transformDownListener: Animation.AnimationListener?
): TransformAnimation {
    private val alphaUp by lazy { AlphaAnimation(endAlphaValue, startAlphaValue) }
    private val alphaDown by lazy { AlphaAnimation(startAlphaValue, endAlphaValue) }

    init {
        alphaUp.duration = duration
        alphaUp.startOffset = startOffSet
        alphaUp.fillAfter = fillAfter
        alphaUp.setAnimationListener(transformUpListener)

        alphaDown.duration = duration
        alphaDown.startOffset = startOffSet
        alphaDown.fillAfter = fillAfter
        alphaDown.setAnimationListener(transformDownListener)
    }

    override fun transformUp(view: View) {
        view.startAnimation(alphaUp)
    }

    override fun transformDown(view: View) {
        view.startAnimation(alphaDown)
    }

    override fun getUpAnimation() = alphaUp

    override fun getDownAnimation() = alphaDown

    class TransformAlphaBuilder: TransformAnimationBuilder() {
        private var startAlphaValue = 1f
        private var endAlphaValue = 0f

        fun setStartAlphaValue(startAlpha: Float): TransformAlphaBuilder {
            startAlphaValue = startAlpha

            return this
        }

        fun setEndAlphaValue(endAlpha: Float): TransformAlphaBuilder {
            endAlphaValue = endAlpha

            return this
        }

        override fun build(): TransformAnimation =
            TransformAlphaAnimation(
                startAlphaValue,
                endAlphaValue,
                animateDuration,
                animateStartOffSet,
                animateFillAfter,
                getTransformListener(transformUpStartAnimation, transformUpEndAnimation),
                getTransformListener(transformDownStartAnimation, transformDownEndAnimation)
            )
    }
}