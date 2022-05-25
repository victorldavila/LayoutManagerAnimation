package com.example.homeanimationpoc

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet

class TransformAnimationsSet(
    private val transformUpAnimationSet: AnimationSet,
    private val transformDownAnimationSet: AnimationSet
): TransformAnimation {
    override fun transformUp(view: View) {
        view.startAnimation(transformUpAnimationSet)
    }

    override fun transformDown(view: View) {
        view.startAnimation(transformDownAnimationSet)
    }

    override fun getUpAnimation(): Animation = transformUpAnimationSet

    override fun getDownAnimation(): Animation = transformDownAnimationSet

    class TransformsSetBuilder {
        private val transformUpAnimationSet: AnimationSet by lazy { AnimationSet(isSetShareInterpolator) }
        private val transformDownAnimationSet: AnimationSet by lazy { AnimationSet(isSetShareInterpolator) }

        private var isSetShareInterpolator = true

        init {
            transformUpAnimationSet.fillAfter = true
            transformUpAnimationSet.isFillEnabled = true

            transformDownAnimationSet.fillAfter = true
            transformDownAnimationSet.isFillEnabled = true
        }

        fun isSetShareInterpolator(isSetShareInterpolator: Boolean): TransformsSetBuilder {
            this.isSetShareInterpolator = isSetShareInterpolator

            return this@TransformsSetBuilder
        }

        fun addUpAnimation(animation: Animation): TransformsSetBuilder {
            transformUpAnimationSet.addAnimation(animation)

            return this@TransformsSetBuilder
        }

        fun addDownAnimation(animation: Animation): TransformsSetBuilder {
            transformDownAnimationSet.addAnimation(animation)

            return this@TransformsSetBuilder
        }

        fun buildSetTransform(): TransformAnimationsSet =
            TransformAnimationsSet(
                transformUpAnimationSet,
                transformDownAnimationSet
            )
    }
}