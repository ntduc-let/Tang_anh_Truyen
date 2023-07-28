package com.android.viewpager2transformer.banner

import android.view.View
import com.android.viewpager2transformer.banner.base.ABaseTransformer

open class RotateDownTransformer : ABaseTransformer() {
  override val isPagingEnabled: Boolean
    get() = true
  
  override fun onTransform(page: View, position: Float) {
    val rotation = ROT_MOD * position
    
    page.apply {
      val width = width.toFloat()
      val height = height.toFloat()
      
      pivotX = width * 0.5f
      pivotY = height
      this.rotation = rotation
    }
  }
  
  companion object {
    private const val ROT_MOD = 18.75f
  }
}