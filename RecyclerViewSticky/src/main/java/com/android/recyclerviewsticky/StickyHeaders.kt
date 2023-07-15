package com.android.recyclerviewsticky

import android.view.View

interface StickyHeaders {
  fun isStickyHeader(position: Int): Boolean
  
  interface ViewSetup {
    fun setupStickyHeaderView(stickyHeader: View)
    
    fun teardownStickyHeaderView(stickyHeader: View)
  }
}