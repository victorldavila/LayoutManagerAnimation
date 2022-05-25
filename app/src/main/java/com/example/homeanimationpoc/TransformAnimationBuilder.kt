package com.example.homeanimationpoc

import android.view.animation.Animation

abstract class TransformAnimationBuilder {
    var animateDuration: Long = 1000
    var animateStartOffSet: Long = 50
    var animateFillAfter: Boolean = true

    var transformUpStartAnimation: (() -> Unit)? = null
    var transformUpEndAnimation: (() -> Unit)? = null
    var transformDownStartAnimation: (() -> Unit)? = null
    var transformDownEndAnimation: (() -> Unit)? = null

    fun setTransformUpStartListener(listener: (() -> Unit)? = null): TransformAnimationBuilder {
        this.transformUpStartAnimation = listener

        return this@TransformAnimationBuilder
    }

    fun setTransformUpEndListener(listener: (() -> Unit)? = null): TransformAnimationBuilder {
        this.transformUpEndAnimation = listener

        return this@TransformAnimationBuilder
    }

    fun setTransformDownStartListener(listener: (() -> Unit)? = null): TransformAnimationBuilder {
        this.transformDownStartAnimation = listener

        return this@TransformAnimationBuilder
    }

    fun setTransformDownEndListener(listener: (() -> Unit)? = null): TransformAnimationBuilder {
        this.transformDownEndAnimation = listener

        return this@TransformAnimationBuilder
    }

    fun setDuration(duration: Long): TransformAnimationBuilder {
        animateDuration = duration

        return this@TransformAnimationBuilder
    }

    fun setStartOffSet(startOffSet: Long): TransformAnimationBuilder {
        animateStartOffSet = startOffSet

        return this@TransformAnimationBuilder
    }

    fun setFillAfter(fillAfter: Boolean): TransformAnimationBuilder {
        animateFillAfter = fillAfter

        return this@TransformAnimationBuilder
    }

    fun getTransformListener(
        startAnimation: (() -> Unit)?,
        endAnimation: (() -> Unit)?
    ): Animation.AnimationListener = object : Animation.AnimationListener{
        override fun onAnimationStart(animation: Animation?) {
            startAnimation?.invoke()
        }

        override fun onAnimationEnd(animation: Animation?) {
            endAnimation?.invoke()
        }

        override fun onAnimationRepeat(animation: Animation?) { }
    }

    abstract fun build(): TransformAnimation
}