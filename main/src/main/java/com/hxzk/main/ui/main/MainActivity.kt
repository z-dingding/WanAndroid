package com.hxzk.main.ui.main

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.view.ViewCompat.setFitsSystemWindows
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.gyf.immersionbar.ImmersionBar
import com.hxzk.base.util.AndroidVersion
import com.hxzk.main.R
import com.hxzk.main.event.MessageEvent
import com.hxzk.main.event.TransparentStatusBarEvent
import com.hxzk.main.ui.adapter.ViewPagerFragmentAdapter
import com.hxzk.main.ui.answer.AnswerFragment
import com.hxzk.main.ui.base.BaseActivity
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.main.ui.home.HomeFragment
import com.hxzk.main.ui.mine.MineFragment
import com.hxzk.main.ui.publics.PublicFragment
import com.hxzk.main.ui.system.SystemFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ImmersionBar.with(this).init()
    }

    override fun setupViews(){
        initVp()
        bnv.setOnNavigationItemSelectedListener {
            vp.setCurrentItem(it.order,false)
            true
        }
    }

    private fun initVp() {
        val frags = LinkedList<Fragment>()
        frags.add(BaseFragment.getInstance(HomeFragment::class.java, null)!!)
        frags.add(BaseFragment.getInstance(AnswerFragment::class.java, null)!!)
        frags.add(BaseFragment.getInstance(SystemFragment::class.java, null)!!)
        frags.add(BaseFragment.getInstance(PublicFragment::class.java, null)!!)
        frags.add(BaseFragment.getInstance(MineFragment::class.java, null)!!)
        val vpAdapter =  ViewPagerFragmentAdapter(this, frags)
        vp.apply {
            adapter = vpAdapter
            //是否禁止左右滑动,true允许左右滑动
            isUserInputEnabled = true
            //如果允许滑动则默认不进行预加载
            //ViewPager2最小offscreenPageLimit可以设置为0
            //vp.offscreenPageLimit = 0
            //默认加载第一个Fragment,并隐藏中间页
            setCurrentItem(0,false)
        }
        //监听滑动
        vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
               val menuItem  = bnv.getMenu().getItem(position)
                menuItem.isChecked = true
            }
        })
    }

}