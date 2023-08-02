package com.android.ntduc.baseproject.utils.lottie

import android.graphics.drawable.Drawable
import com.airbnb.lottie.LottieDrawable

class FrameCreator(private var lottieDrawable: LottieDrawable) {

    private val durationInFrames: Int = lottieDrawable.composition.durationFrames.toInt()
    private var currentFrame: Int = 0

    fun generateFrame(): Drawable {
        lottieDrawable.frame = currentFrame
        ++currentFrame
        return lottieDrawable
    }

    fun hasEnded() = currentFrame > durationInFrames
}