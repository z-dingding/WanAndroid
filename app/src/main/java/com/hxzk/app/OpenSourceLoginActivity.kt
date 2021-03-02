package com.hxzk.app

import android.os.Bundle
import android.transition.Transition
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.hxzk.base.util.AndroidVersion

import com.hxzk.main.callback.SimpleTransitionListener
import com.hxzk.main.event.FinishActivityEvent
import com.hxzk.main.ui.activity.login.LoginActivity
import com.hxzk.main.ui.adapter.AdapterFragmentPager
import com.hxzk.main.util.RotateTransformer
import kotlinx.android.synthetic.main.activity_open_source_login.*
import org.greenrobot.eventbus.EventBus

class OpenSourceLoginActivity : LoginActivity() {


    /**
     * 是否正在进行transition动画。
     */
    protected var isTransitioning = false


    override fun onCreate(savedInstanceState: Bundle?) {
        //overridePendingTransition(R.anim.anim_login_in,R.anim.anim_login_silent)
        super.onCreate(savedInstanceState)
        viewPager2 =vp2
        setContentView(R.layout.activity_open_source_login)

    }

    override fun setupViews() {
        //Android5.0及其以上版本且有动画效果
        if (AndroidVersion.hasLollipop()) {
            isTransitioning = true
            //转场动画监听
            window.sharedElementEnterTransition.addListener(object : SimpleTransitionListener() {
                override fun onTransitionEnd(transition: Transition) {
                    val event = FinishActivityEvent()
                    event.activityClass = OpenSourceSplashActivity::class.java
                    EventBus.getDefault().post(event)
                    isTransitioning = false
                }
            })
        }

        close.setOnClickListener {
            if (!isTransitioning) {
                finish()
            }
        }
        initVP()
    }


    /**
     * 初始化ViewPager
     */
    fun initVP(){
        viewPager2.adapter = AdapterFragmentPager(this)
        viewPager2.offscreenPageLimit = 1
        viewPager2.isUserInputEnabled=false
        //设置页面间距
        viewPager2.setPageTransformer(
            MarginPageTransformer(0)
        )
        //设置页面切换动画
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(RotateTransformer())
        viewPager2.setPageTransformer(compositePageTransformer)
//        vp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                Toast.makeText(this@LoginActivity, "page selected $position", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        })
    }




    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_login_in,R.anim.anim_login_out)
    }

}