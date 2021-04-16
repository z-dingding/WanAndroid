package com.hxzk.main.ui.splash

import android.view.View
import com.hxzk.base.extension.actionFinish
import com.hxzk.base.util.Preference
import com.hxzk.main.event.FinishActivityEvent
import com.hxzk.main.event.MessageEvent
import com.hxzk.main.ui.base.BaseActivity
import com.hxzk.main.ui.login.LoginActivity
import com.hxzk.main.ui.main.MainActivity
import com.hxzk.network.NetWork
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.concurrent.thread

open  class SplashActivity : BaseActivity() {

    lateinit var logoView: View

    override fun setupViews() {
        delayTimeStartAction()
    }

    /**
     * 延迟执行动画跳转
     */
    private fun delayTimeStartAction(){
        thread {
            Thread.sleep(WAIT_TIME)
            //线程睡醒了执行跳转
            runOnUiThread {
                val account  by Preference<String>(NetWork.KEY_COOKIES,"")
                if(account.isNotEmpty()){
                    activity?.actionFinish<MainActivity>(this)
                }else {
                    LoginActivity.startActionWithTransition(this, logoView)
                }
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
        const val  WAIT_TIME = 2000L
    }

}