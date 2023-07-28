package com.android.viewpager2transformer.banner

import android.view.View
import com.android.viewpager2transformer.banner.base.ABaseTransformer

open class CubeOutTransformer @JvmOverloads constructor(
  private val distanceMultiplier: Int = 20
) : ABaseTransformer() {
  public override val isPagingEnabled: Boolean
    get() = true
  
  override fun onTransform(page: View, position: Float) {
    page.apply {
      cameraDistance = (width * distanceMultiplier).toFloat()
      pivotX = if (position < 0f) width.toFloat() else 0f
      pivotY = height * 0.5f
      rotationY = 90f * position
    }
  }
}