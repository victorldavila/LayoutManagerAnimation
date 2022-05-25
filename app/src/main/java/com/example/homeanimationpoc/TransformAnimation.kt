package com.example.homeanimationpoc

import android.view.View
import android.view.animation.Animation

interface TransformAnimation {
    fun transformUp(view: View)
    fun transformDown(view: View)
    fun getUpAnimation(): Animation
    fun getDownAnimation(): Animation
}