package com.hxzk.main.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.*

/**
 *作者：created by zjt on 2021/4/1
 *描述: 首页Viewpager适配器
 *
 */
class ViewPagerFragmentAdapter(
     fragmentActivity: FragmentActivity,
    private val listFrags: LinkedList<Fragment>
) : FragmentStateAdapter(fragmentActivity){




    override fun getItemCount(): Int {
       return listFrags.size
    }

    override fun createFragment(position: Int): Fragment {
       return listFrags.get(position)
    }
}