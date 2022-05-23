package com.example.homeanimationpoc

import android.view.View
import android.view.animation.AlphaAnimation

class AlphaTransformAnimation: TransformAnimation {
    private val alphaUpAnimation by lazy { AlphaAnimation(endAlphaValue, startAlphaValue) }
    private val alphaDownAnimation by lazy { AlphaAnimation(startAlphaValue, endAlphaValue) }

    private var startAlphaValue = 1f
    private var endAlphaValue = 0f

    private var alphaAnimationDuration: Long = 1000
    private var alphaAnimationStartOffSet: Long = 50
    private var alphaAnimationFillAfter: Boolean = true

    fun setStartAlphaValue(startAlpha: Float): AlphaTransformAnimation {
        startAlphaValue = startAlpha

        return this
    }

    fun setEndAlphaValue(endAlpha: Float): AlphaTransformAnimation {
        endAlphaValue = endAlpha

        return this
    }

    fun setAlphaDuration(duration: Long): AlphaTransformAnimation {
        alphaAnimationDuration = duration

        return this
    }

    fun setAlphaStartOffSet(startOffSet: Long): AlphaTransformAnimation {
        alphaAnimationStartOffSet = startOffSet

        return this
    }

    fun setAlphaFillAfter(fillAfter: Boolean): AlphaTransformAnimation {
        alphaAnimationFillAfter = fillAfter

        return this
    }

    fun buildTransformation(): AlphaTransformAnimation {
        alphaUpAnimation.duration = alphaAnimationDuration
        alphaUpAnimation.startOffset = alphaAnimationStartOffSet
        alphaUpAnimation.fillAfter = alphaAnimationFillAfter

        alphaDownAnimation.duration = alphaAnimationDuration
        alphaDownAnimation.startOffset = alphaAnimationStartOffSet
        alphaDownAnimation.fillAfter = alphaAnimationFillAfter

        return this
    }

    override fun upTransform(view: View) {
        view.startAnimation(alphaUpAnimation)
    }

    override fun downTransform(view: View) {
        view.startAnimation(alphaDownAnimation)
    }
}