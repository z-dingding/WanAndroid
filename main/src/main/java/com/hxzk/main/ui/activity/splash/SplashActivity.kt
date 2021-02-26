package com.hxzk.main.ui.activity.splash

import android.os.Bundle
import com.hxzk.main.R
import com.hxzk.main.event.FinishActivityEvent
import com.hxzk.main.event.MessageEvent
import com.hxzk.main.ui.activity.base.BaseActivity
import com.hxzk.main.ui.activity.login.LoginActivity
import kotlinx.android.synthetic.main.activity_splash.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.concurrent.thread

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun setupViews() {
        delayTimeStartAction()
    }

    fun delayTimeStartAction(){
        thread {
            Thread.sleep(WAIT_TIME)
            //线程睡醒了执行跳转
            runOnUiThread {
                LoginActivity.startActionWithTransition(this, ivLogo)
            }
        }
    }

    override fun onBackPressed() {
        // 屏蔽手机的返回键
    }

    //动画跳转到下个界面,异步通知销毁此Activity
    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onMessageEvent(messageEvent: MessageEvent) {
        if (messageEvent is FinishActivityEvent) {
            if (javaClass == messageEvent.activityClass) {
                if (!isFinishing) {
                    finish()
                }
            }
        }
    }

    companion object{
        /**
         * 此界面停留时间
         */
        const val  WAIT_TIME = 3000L
    }

}