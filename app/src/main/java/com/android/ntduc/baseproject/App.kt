package com.android.ntduc.baseproject

import android.app.Application
import com.orhanobut.hawk.Hawk
import com.proxglobal.ads.application.ProxApplication
import com.proxglobal.proxads.ads.application.AdsApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class App : AdsApplication() {
    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
    }
}

