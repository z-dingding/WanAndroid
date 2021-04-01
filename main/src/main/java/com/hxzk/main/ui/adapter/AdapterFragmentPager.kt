package com.hxzk.main.ui.adapter

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.main.ui.login.LoginFragment
import com.hxzk.main.ui.register.RegisterFragment


/**
 *作者：created by zjt on 2021/3/2
 *描述:登录和注册的Fragment
 *
 */
class AdapterFragmentPager( fragmentActivity : FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragments: SparseArray<Fragment> = SparseArray()

 init {
    fragments.put(PAGE_HOME, BaseFragment.getInstance(LoginFragment:: class.java , null))
    fragments.put(PAGE_FIND, BaseFragment.getInstance(RegisterFragment:: class.java , null))
 }
    override fun getItemCount(): Int {
       return fragments.size()
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    companion object{
        const val PAGE_HOME = 0
        const val PAGE_FIND = 1
    }
}