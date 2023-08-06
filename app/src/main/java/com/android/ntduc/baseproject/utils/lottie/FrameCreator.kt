package com.android.ntduc.baseproject.utils.lottie

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import com.airbnb.lottie.LottieDrawable

class FrameCreator(private var lottieDrawable: LottieDrawable) {

    private val durationInFrames: Int = lottieDrawable.composition.durationFrames.toInt()
    private var currentFrame: Int = 0

    init {
//        lottieDrawable.bounds = Rect()
        val rect = lottieDrawable.bounds
        Log.d("ntduc_debug", "left: ${rect.left}")
        Log.d("ntduc_debug", "right: ${rect.right}")
        Log.d("ntduc_debug", "bottom: ${rect.bottom}")
        Log.d("ntduc_debug", "top: ${rect.top}")
    }

    fun generateFrame(): Drawable {
        lottieDrawable.frame = currentFrame
        ++currentFrame

        Log.d("ntduc_debug", "generateFrame: $currentFrame, ${lottieDrawable.intrinsicWidth}, ${lottieDrawable.intrinsicHeight}")
        return lottieDrawable
    }

    fun hasEnded() = currentFrame > durationInFrames
}