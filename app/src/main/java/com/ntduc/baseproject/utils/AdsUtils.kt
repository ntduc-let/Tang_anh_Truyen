package com.ntduc.baseproject.utils

object AdsUtils {
//    fun isPurchased(): Boolean {
//        return ProxPurchase.getInstance().checkPurchased()
//    }
//
//    fun loadInterAds(activity: Activity, idShowAds: String) {
//        if (!NetworkUtil.isConnection(activity)) {
//            return
//        }
//        ProxAdsCache.instance.loadInterstitialAds(activity, idShowAds, object : LoadAdsCallback() {
//            override fun onLoadFailed(message: String?) {
//                super.onLoadFailed(message)
//                Log.d("ntduc_debug", "$idShowAds onLoadFailed: $message")
//            }
//
//            override fun onLoadSuccess() {
//                super.onLoadSuccess()
//                Log.d("ntduc_debug", "$idShowAds onLoadSuccess: ")
//            }
//        })
//    }
//
//    fun loadRewardAds(activity: Activity, idShowAds: String) {
//        if (!NetworkUtil.isConnection(activity)) {
//            return
//        }
//        ProxAdsCache.instance.loadRewardAds(activity, idShowAds, object : LoadAdsCallback() {
//            override fun onLoadFailed(message: String?) {
//                super.onLoadFailed(message)
//                Log.d("ntduc_debug", "$idShowAds onLoadFailed: $message")
//            }
//
//            override fun onLoadSuccess() {
//                super.onLoadSuccess()
//                Log.d("ntduc_debug", "$idShowAds onLoadSuccess: ")
//            }
//        })
//    }
//
//    fun showInterstitialAds(activity: Activity, idShowAds: String, onAdsClosed: () -> Unit) {
//        if (!NetworkUtil.isConnection(activity)) {
//            onAdsClosed()
//            return
//        }
//        ProxAdsCache.instance.showInterstitialAds(activity, idShowAds, object : ShowAdsCallback() {
//            override fun onShowFailed(message: String?) {
//                super.onShowFailed(message)
//                Log.d("ntduc_debug", "$idShowAds onShowFailed: $message")
//                onAdsClosed()
//            }
//
//            override fun onAdClosed() {
//                super.onAdClosed()
//                Log.d("ntduc_debug", "$idShowAds onAdClosed: ")
//                onAdsClosed()
//            }
//        })
//    }
//
//    fun showSplashAds(activity: Activity, onAdsClosed: () -> Unit) {
//        if (!NetworkUtil.isConnection(activity)) {
//            onAdsClosed()
//            return
//        }
//        ProxAdsCache.instance.showSplashAds(activity, object : ShowAdsCallback() {
//            override fun onAdClosed() {
//                super.onAdClosed()
//                onAdsClosed()
//            }
//
//            override fun onShowFailed(message: String?) {
//                super.onShowFailed(message)
//                onAdsClosed()
//            }
//        })
//    }
//
//    fun showBannerAds(activity: Activity, container: FrameLayout, idShowAds: String): BannerAds<*>? {
//        if (!NetworkUtil.isConnection(activity)) {
//            return null
//        }
//        var banner: BannerAds<*>? = null
//        banner = ProxAdsCache.instance.loadBannerAds(activity, container, idShowAds, object : LoadAdsCallback() {
//            override fun onLoadSuccess() {
//                super.onLoadSuccess()
//                Log.d("ntduc_debug", "$idShowAds onLoadSuccess: ")
//                banner?.showAds(container)
//            }
//
//            override fun onLoadFailed(message: String?) {
//                super.onLoadFailed(message)
//                Log.d("ntduc_debug", "$idShowAds onLoadFailed: $message")
//            }
//        }
//        )
//        return banner
//    }
//
//    fun showRewardAds(activity: Activity, idShowAds: String, onAdsGranted: () -> Unit) {
//        if (!NetworkUtil.isConnection(activity)) {
//            onAdsGranted()
//            return
//        }
//        var grantRewards = false
//        ProxAdsCache.instance.showRewardAds(activity, idShowAds, object : ShowAdsCallback() {
//            override fun onAdClosed() {
//                super.onAdClosed()
//                Log.d("ntduc_debug", "$idShowAds onAdClosed: ")
//                if (grantRewards) {
//                    onAdsGranted()
//                }
//            }
//
//            override fun onShowFailed(message: String?) {
//                super.onShowFailed(message)
//                Log.d("ntduc_debug", "$idShowAds onShowFailed: $message")
//                onAdsGranted()
//            }
//
//            override fun onGetReward(amount: Int, type: String) {
//                super.onGetReward(amount, type)
//                Log.d("ntduc_debug", "$idShowAds onGetReward: ")
//                grantRewards = true
//            }
//        })
//    }
//
//    fun showNativeAds(activity: Activity, container: FrameLayout, idShowAds: String): NativeAds<*>? {
//        if (!NetworkUtil.isConnection(activity)) {
//            return null
//        }
//        var native: NativeAds<*>? = null
//        native = ProxAdsCache.instance.loadNativeAds(activity, container, idShowAds, object : LoadAdsCallback() {
//            override fun onLoadFailed(message: String?) {
//                super.onLoadFailed(message)
//                container.removeAllViews()
//            }
//
//            override fun onLoadSuccess() {
//                super.onLoadSuccess()
//                native?.showAds(container)
//            }
//        }
//        )
//        return native
//    }
//
//    fun showRate(activity: AppCompatActivity, alwaysShow: Boolean = false, onClosed: (() -> Unit)? = null) {
//        try {
//            val config = ProxRateConfig()
//            config.listener = object : RatingDialogListener() {
//                override fun onSubmitButtonClicked(rate: Int, comment: String) {
//                    super.onSubmitButtonClicked(rate, comment)
//                    Firebase.analytics.logEvent("prox_rating_layout") {
//                        param("event_type", "rated")
//                        param("star", "$rate star")
//                        param("comment", comment)
//                    }
//                }
//
//                override fun onLaterButtonClicked() {
//                    super.onLaterButtonClicked()
//                    onClosed?.invoke()
//                }
//
//                override fun onRated() {
//                    super.onRated()
//                    onClosed?.invoke()
//                }
//
//                override fun onDone() {
//                    super.onDone()
//                    onClosed?.invoke()
//                }
//            }
//            ProxRateDialog.init()
//            ProxRateDialog.setConfig(config)
//            if (alwaysShow) ProxRateDialog.showAlways(activity.supportFragmentManager)
//            else ProxRateDialog.showIfNeed(activity, activity.supportFragmentManager)
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//            onClosed?.invoke()
//        }
//    }
}