package com.android.viewpager2transformer

import android.view.View

open class AccordionTransformer : ABaseTransformer() {
  override fun onTransform(page: View, position: Float) {
    page.apply {
      pivotX = if (position < 0) 0f else width.toFloat()
      scaleX = if (position < 0) 1f + position else 1f - position
    }
  }
}