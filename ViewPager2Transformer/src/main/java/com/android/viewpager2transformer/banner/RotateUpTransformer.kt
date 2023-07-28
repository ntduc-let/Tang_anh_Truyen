package com.android.viewpager2transformer.banner

import android.view.View
import com.android.viewpager2transformer.banner.base.ABaseTransformer

open class RotateUpTransformer : ABaseTransformer() {
  override val isPagingEnabled: Boolean
    get() = true
  
  override fun onTransform(page: View, position: Float) {
    val rotation = ROT_MOD * position
    
    page.apply {
      val width = width.toFloat()
      
      pivotX = width * 0.5f
      pivotY = 0f
      translationX = 0f
      this.rotation = rotation
    }
  }
  
  companion object {
    private const val ROT_MOD = -15f
  }
}