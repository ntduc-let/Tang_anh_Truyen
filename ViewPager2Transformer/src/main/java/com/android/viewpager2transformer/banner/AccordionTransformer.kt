package com.android.viewpager2transformer.banner

import android.view.View
import com.android.viewpager2transformer.banner.base.ABaseTransformer

open class AccordionTransformer : ABaseTransformer() {
  override fun onTransform(page: View, position: Float) {
    page.apply {
      pivotX = if (position < 0) 0f else width.toFloat()
      scaleX = if (position < 0) 1f + position else 1f - position
    }
  }
}