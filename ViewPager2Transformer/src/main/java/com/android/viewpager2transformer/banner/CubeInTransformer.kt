package com.android.viewpager2transformer.banner

import android.view.View
import com.android.viewpager2transformer.banner.base.ABaseTransformer

open class CubeInTransformer : ABaseTransformer() {
  public override val isPagingEnabled: Boolean
    get() = true
  
  override fun onTransform(page: View, position: Float) {
    // Rotate the fragment on the left or right edge
    page.apply {
      pivotX = if (position > 0) 0f else width.toFloat()
      pivotY = 0f
      rotationY = -90f * position
    }
  }
}