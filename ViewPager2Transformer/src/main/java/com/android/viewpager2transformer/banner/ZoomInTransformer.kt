package com.android.viewpager2transformer.banner

import android.view.View
import com.android.viewpager2transformer.banner.base.ABaseTransformer
import kotlin.math.abs

open class ZoomInTransformer : ABaseTransformer() {
  override fun onTransform(page: View, position: Float) {
    val scale = if (position < 0) position + 1f else abs(1f - position)
    
    page.apply {
      scaleX = scale
      scaleY = scale
      pivotX = width * 0.5f
      pivotY = height * 0.5f
      alpha = if (position < -1f || position > 1f) 0f else 1f - (scale - 1f)
    }
  }
}