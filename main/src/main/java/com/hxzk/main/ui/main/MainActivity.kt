package com.hxzk.main.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.hxzk.main.R
import com.hxzk.main.ui.adapter.ViewPagerFragmentAdapter
import com.hxzk.main.ui.base.BaseActivity
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.main.ui.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun setupViews(){
        setupToolbar()
        //首页隐藏返回按钮
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        initVp()
        bnv.setOnNavigationItemSelectedListener {
            vp.setCurrentItem(it.order,false)
            true
        }
    }

    private fun initVp() {
        val frags = LinkedList<Fragment>()
        frags.add(BaseFragment.getInstance(HomeFragment::class.java, null)!!)
        frags.add(BaseFragment.getInstance(HomeFragment::class.java, null)!!)
        frags.add(BaseFragment.getInstance(HomeFragment::class.java, null)!!)
        frags.add(BaseFragment.getInstance(HomeFragment::class.java, null)!!)
        frags.add(BaseFragment.getInstance(HomeFragment::class.java, null)!!)
        val vpAdapter =  ViewPagerFragmentAdapter(this, frags)
        vp.apply {
            adapter = vpAdapter
            //禁止左右滑动
            isUserInputEnabled = true
            //如果允许滑动则默认不进行预加载
            //vp.offscreenPageLimit = 1
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