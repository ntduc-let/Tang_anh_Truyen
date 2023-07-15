package com.ntduc.baseproject.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.skydoves.bindables.BindingActivity

abstract class BaseActivity<T : ViewDataBinding> constructor(
    @LayoutRes val contentLayoutId: Int
) : BindingActivity<T>(contentLayoutId) {

//    var banner: BannerAds<*>? = null
//    var native: NativeAds<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        addEvent()
        addObservers()
        initData()
    }

    open fun initView() {}

    open fun addEvent() {}

    open fun addObservers() {}

    open fun initData() {}

    override fun onResume() {
        super.onResume()
//        banner?.resumeAds()
    }

    override fun onPause() {
//        banner?.pauseAds()
        super.onPause()
    }

    override fun onDestroy() {
//        banner?.destroyAds()
//        native?.destroyAds()
        super.onDestroy()
    }
}
