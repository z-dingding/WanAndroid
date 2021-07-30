package com.hxzk.main.ui.notify

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hxzk.main.R
import com.hxzk.main.databinding.ActivityNotifyBinding
import com.hxzk.main.event.MessageEvent
import com.hxzk.main.event.RegisterSuccessEvent
import com.hxzk.main.event.UnReadNumEvent
import com.hxzk.main.ui.base.BaseActivity
import com.hxzk.main.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_mine.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class NotifyActivity : BaseActivity() {

    lateinit var databing : ActivityNotifyBinding
    lateinit var tabs : Array<String>
    private val tabFragmentList: ArrayList<Fragment> = ArrayList()
    lateinit var mediator: TabLayoutMediator

    private val activeSize = 20f
    private val normalSize = 14f
    private val activeColor: Int = android.graphics.Color.parseColor("#ff678f")
    private val normalColor: Int = android.graphics.Color.parseColor("#666666")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databing= DataBindingUtil.setContentView(this,R.layout.activity_notify)
        setContentView(databing.root)
        initial()
    }

    private fun initial() {
        setupToolbar(title = resources.getString(R.string.title_notify))
        initVPAndTab()
    }

    /**
     * 初始化ViewPager
     */
    private fun initVPAndTab(){
        tabs =arrayOf(resources.getString(R.string.notify_read),resources.getString(R.string.notify_unread))
        tabFragmentList.apply {
            add(BaseFragment.getInstance(ReadFragment ::class.java,null))
            add(BaseFragment.getInstance(UnReadFragment ::class.java,null))
        }

        databing.viewpager.adapter = object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
            override fun createFragment(position: Int): Fragment {
                //FragmentStateAdapter内部自己会管理已实例化的fragment对象。
                // 所以不需要考虑复用的问题
                return tabFragmentList[position]
            }

            override fun getItemCount(): Int {
                return tabFragmentList.size
            }
        }
        databing.viewpager.offscreenPageLimit = OFFSCREEN_PAGE_LIMIT_DEFAULT
        //viewPager 页面切换监听
        databing.viewpager.registerOnPageChangeCallback(changeCallback);
        //设置Tablayout相关
        mediator = TabLayoutMediator(databing.tabLayout, databing.viewpager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            //这里可以自定义TabView
            val tabView = TextView(this)
            val states = arrayOfNulls<IntArray>(2)
            states[0] = intArrayOf(android.R.attr.state_selected)
            states[1] = intArrayOf()
            val colors = intArrayOf(activeColor, normalColor)
            val colorStateList = ColorStateList(states, colors)
            tabView.text = tabs[position]
            tabView.textSize = normalSize
            tabView.setTextColor(colorStateList)
            tab.customView = tabView
        })
        //要执行这一句才是真正将两者绑定起来
        mediator.attach()
    }


    private val changeCallback: OnPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            //可以来设置选中时tab的大小
            val tabCount: Int = databing.tabLayout.tabCount
            for (i in 0 until tabCount) {
                val tab: TabLayout.Tab = databing.tabLayout.getTabAt(i)!!
                val tabView: TextView? = tab.customView as TextView?
                if (tab.position == position) {
                    tabView?.textSize = activeSize
                    tabView?.setTypeface(Typeface.DEFAULT_BOLD)
                } else {
                    tabView?.textSize = normalSize
                    tabView?.setTypeface(Typeface.DEFAULT)
                }
            }
        }
    }




    override fun onDestroy() {
        mediator.detach()
        databing.viewpager.unregisterOnPageChangeCallback(changeCallback)
        super.onDestroy()
    }


}