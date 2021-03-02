package com.hxzk.main.ui.activity.login

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.hxzk.base.util.AndroidVersion
import com.hxzk.main.R
import com.hxzk.main.ui.activity.base.BaseActivity

  open class  LoginActivity : BaseActivity() {
     lateinit var viewPager2 : ViewPager2

      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
      }
     /**
      * 切换Fragment
      * fakeDragBy接受一个float的参数，
      * 当参数值为正数时表示向前一个页面滑动，当值为负数时表示向下一个页面滑动。
      */
     fun fakeDragBy(number: Float) {
         //先beginFakeDrag方法来开启模拟拖拽
         viewPager2.beginFakeDrag()
         if (viewPager2.fakeDragBy(number))
         //endFakeDrag方法来关闭模拟拖拽
             viewPager2.endFakeDrag()
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
            activity.finish()
        }
    }

      override fun setupViews() {
          TODO("Not yet implemented")
      }


  }