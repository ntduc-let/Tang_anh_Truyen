package com.android.viewpager2transformer.card

import android.view.View
import androidx.viewpager2.widget.ViewPager2

open class RotateDownPageTransformer : ViewPager2.PageTransformer {
  private val DEFAULT_MAX_ROTATE = 15.0f
  private val mMaxRotate = DEFAULT_MAX_ROTATE

  override fun transformPage(view: View, position: Float) {
    if (position < -1) { // [-Infinity,-1)
      view.rotation = mMaxRotate * -1
      view.pivotX = view.width.toFloat()
      view.pivotY = view.height.toFloat()
    } else if (position <= 1) { // [-1,1]
      if (position < 0) //[0，-1]
      {
        view.pivotX = view.width * (0.5f + 0.5f * -position)
        view.pivotY = view.height.toFloat()
        view.rotation = mMaxRotate * position
      } else  //[1,0]
      {
        view.pivotX = view.width * 0.5f * (1 - position)
        view.pivotY = view.height.toFloat()
        view.rotation = mMaxRotate * position
      }
    } else { // (1,+Infinity]
      view.rotation = mMaxRotate
      view.pivotX = (view.width * 0).toFloat()
      view.pivotY = view.height.toFloat()
    }
  }
}