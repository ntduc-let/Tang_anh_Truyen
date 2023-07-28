package com.android.viewpager2transformer.card

import android.annotation.SuppressLint
import android.view.View
import androidx.viewpager2.widget.ViewPager2


open class CascadingPageTransformer : ViewPager2.PageTransformer {
  private var mScaleOffset = 40

  open fun setScaleOffset(mScaleOffset: Int) {
    this.mScaleOffset = mScaleOffset
  }

  @SuppressLint("NewApi")
  override fun transformPage(page: View, position: Float) {
    if (position <= 0.0f) {
      page.translationX = 0f
      page.rotation = 45 * position
      page.translationX = page.width / 3 * position
    } else {
      val scale = (page.width - mScaleOffset * position) / page.width.toFloat()
      page.scaleX = scale
      page.scaleY = scale
      page.translationX = -page.width * position
      page.translationY = mScaleOffset * 0.8f * position
    }
  }
}