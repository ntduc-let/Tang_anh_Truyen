package com.android.ntduc.baseproject.ui.component.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.android.ntduc.baseproject.R
import com.android.ntduc.baseproject.databinding.ActivitySplashBinding
import com.android.ntduc.baseproject.ui.base.BaseActivity
import com.android.ntduc.baseproject.ui.component.navigation.NavigationActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private val viewModel: SplashViewModel by viewModels()

    override fun addEvent() {
        super.addEvent()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashActivity, NavigationActivity::class.java))
            finish()
        }, 50)
    }

    override fun onBackPressed() {}
}
