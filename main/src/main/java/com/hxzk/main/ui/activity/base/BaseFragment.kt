package com.hxzk.main.ui.activity.base

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 *作者：created by zjt on 2021/3/2
 *描述:
 *
 */
 open class BaseFragment : Fragment() {


    companion object{
        /**
         * 创建fragment的静态方法，方便传递参数
         * @param args 传递的参数
         * @return
         */
        fun <T : Fragment?> getInstance(clazz: Class<*>, args: Bundle?): T? {
            var mFragment: T? = null
            try {
                mFragment = clazz.newInstance() as T
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            if (args != null) {
                mFragment?.setArguments(args)
            }
            return mFragment
        }
    }


}