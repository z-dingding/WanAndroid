package com.hxzk.main.ui.activity.login

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.transition.Transition
import android.view.View
import com.hxzk.base.util.AndroidVersion
import com.hxzk.main.R
import com.hxzk.main.callback.SimpleTransitionListener
import com.hxzk.main.event.FinishActivityEvent
import com.hxzk.main.ui.activity.base.BaseActivity
import com.hxzk.main.ui.activity.main.MainActivity
import com.hxzk.main.ui.activity.register.RegisterActivity
import com.hxzk.main.ui.activity.splash.SplashActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus

class LoginActivity : BaseActivity() {

    /**
     * 是否正在进行transition动画。
     */
    protected var isTransitioning = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun setupViews() {
        //Android5.0及其以上版本且有动画效果
        if (AndroidVersion.hasLollipop()) {
            isTransitioning = true
            //转场动画监听
            window.sharedElementEnterTransition.addListener(object : SimpleTransitionListener() {
                override fun onTransitionEnd(transition: Transition) {
                    val event = FinishActivityEvent()
                    event.activityClass = SplashActivity::class.java
                    EventBus.getDefault().post(event)
                    isTransitioning = false
                }
            })
        }

        loginButton.setOnClickListener {
           MainActivity.actionStart(this)
            finish()
        }

        registerAccaount.setOnClickListener {
           RegisterActivity.startAction(this)
        }

    }



    override fun onBackPressed() {
        if (!isTransitioning) {
            finish()
        }
    }



    companion object {
        @JvmStatic
        val START_WITH_TRANSITION = "start_with_transition"

        fun startActionWithTransition(activity: Activity, logo: View) {
            val mIntent = Intent(activity, LoginActivity::class.java)
            //android5.0支持转场动画
            if (AndroidVersion.hasLollipop()) {
                mIntent.putExtra(START_WITH_TRANSITION, true)
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    activity,
                    logo,
                    activity.getString(R.string.transition_logo_splash)
                )
                activity.startActivity(mIntent, options.toBundle())
            } else {
                activity.startActivity(mIntent)
                activity.finish()
            }
        }

        fun  startAction (activity :Activity){
            val  intent = Intent(activity,LoginActivity ::class.java)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.rotate_start,R.anim.rotate_end)
            activity.finish()
        }
    }
}