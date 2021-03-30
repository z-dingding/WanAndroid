package com.hxzk.main.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.greenrobot.eventbus.EventBus

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        EventBus.getDefault().register(this)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

}