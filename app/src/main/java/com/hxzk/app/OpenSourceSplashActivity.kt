package com.hxzk.app

import android.os.Bundle
import com.hxzk.main.ui.splash.SplashActivity
import kotlinx.android.synthetic.main.activity_open_source_splash.*

class OpenSourceSplashActivity : SplashActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_source_splash)
        logoView = ivLogo
    }
}