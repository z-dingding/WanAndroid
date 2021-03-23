package com.hxzk.main.ui.login

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.view.View
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.hxzk.base.util.AndroidVersion
import com.hxzk.base.util.Common
import com.hxzk.main.R
import com.hxzk.main.ui.adapter.AdapterFragmentPager
import com.hxzk.main.ui.base.BaseActivity
import com.hxzk.main.util.RotateTransformer

open abstract class  LoginActivity : BaseActivity() {

     lateinit var viewPager2 : ViewPager2

      /**
       * 初始化ViewPager
       */
      fun initVP(){
          viewPager2.adapter = AdapterFragmentPager(this)
          viewPager2.offscreenPageLimit = 1
          //禁止用户手势滑动
          viewPager2.isUserInputEnabled=false
          //设置页面间距
          viewPager2.setPageTransformer(
              MarginPageTransformer(0)
          )
          //设置页面切换动画
          val compositePageTransformer = CompositePageTransformer()
          compositePageTransformer.addTransformer(RotateTransformer())
          viewPager2.setPageTransformer(compositePageTransformer)
      }


      /**
      * 切换Fragment:0切换到登录,1切换到注册
      */
     fun  switchFrag(number: Int) {
          if (number == 0) viewPager2.currentItem = 0   else viewPager2.currentItem = 1
     }


    companion object {
        //隐士跳转到OpenSourceLoginActivity
        private val ACTION_LOGIN_WITH_TRANSITION = "${Common.getPackageName()}.ACTION_LOGIN_WITH_TRANSITION"
       //转场动画的标识
        @JvmStatic
        val START_WITH_TRANSITION = "start_with_transition"

        fun startActionWithTransition(activity: Activity, logo: View) {
            //注意此处使用的是隐士跳转(解决不在同意modele无法访问指定Activity的问题)
            val mIntent = Intent(ACTION_LOGIN_WITH_TRANSITION)
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
    }




  }