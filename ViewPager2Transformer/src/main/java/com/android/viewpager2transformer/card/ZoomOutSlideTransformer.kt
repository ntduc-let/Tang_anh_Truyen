package com.android.viewpager2transformer.card

import android.view.View
import com.android.viewpager2transformer.banner.base.ABaseTransformer
import kotlin.math.abs
import kotlin.math.max

open class ZoomOutSlideTransformer : ABaseTransformer() {

  private val MIN_SCALE = 0.85f
  private val MIN_ALPHA = 0.5f

  override fun onTransform(view: View, position: Float) {
    if (position >= -1 || position <= 1) {
      // Modify the default slide transition to shrink the page as well
      val height = view.height.toFloat()
      val width = view.width.toFloat()
      val scaleFactor = max(MIN_SCALE, 1 - abs(position))
      val vertMargin = height * (1 - scaleFactor) / 2
      val horzMargin = width * (1 - scaleFactor) / 2

      // Center vertically
      view.pivotY = 0.5f * height
      view.pivotX = 0.5f * width
      if (position < 0) {
        view.translationX = horzMargin - vertMargin / 2
      } else {
        view.translationX = -horzMargin + vertMargin / 2
      }

      // Scale the page down (between MIN_SCALE and 1)
      view.scaleX = scaleFactor
      view.scaleY = scaleFactor

      // Fade the page relative to its size.
      view.alpha = MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)
    }
  }
}