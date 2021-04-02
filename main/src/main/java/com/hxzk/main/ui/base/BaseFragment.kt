package com.hxzk.main.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 *作者：created by zjt on 2021/3/2
 *描述:
 *
 */
 open class BaseFragment : Fragment() {

    /**
     * Fragment中inflate出来的布局。
     */
    protected var rootView: View? = null




    /**
     * 在Fragment基类中获取通用的控件，会将传入的View实例原封不动返回。
     * @param view
     * Fragment中inflate出来的View实例。
     * @return  Fragment中inflate出来的View实例原封不动返回。
     */
    fun onCreateView(view: View): View {
        rootView = view
        return view
    }


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
                mFragment?.arguments = args
            }
            return mFragment
        }
    }

}