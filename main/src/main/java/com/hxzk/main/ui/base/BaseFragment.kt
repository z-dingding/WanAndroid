package com.hxzk.main.ui.base

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.hxzk.main.callback.PermissionListener
import java.util.ArrayList

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

    private var mListener: PermissionListener? = null

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
        fun <T : Fragment> getInstance(clazz: Class<*>, args: Bundle?): T {
            val  mFragment = clazz.newInstance() as T
            if (args != null) {
                mFragment.arguments = args
            }
            return mFragment
        }
    }


    /**
     * 检查和处理运行时权限，并将用户授权的结果通过PermissionListener进行回调。
     *
     * @param permissions
     * 要检查和处理的运行时权限数组
     * @param listener
     * 用于接收授权结果的监听器
     */
    protected fun handlePermissions(permissions: Array<String>?, listener: PermissionListener) {
        if (permissions == null || activity == null) {
            return
        }
        mListener = listener
        val requestPermissionList = ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(activity!!, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionList.add(permission)
            }
        }
        if (!requestPermissionList.isEmpty()) {
            requestPermissions(requestPermissionList.toTypedArray(), 1)
        } else {
            listener.onGranted()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> if (grantResults.isNotEmpty()) {
                val deniedPermissions = ArrayList<String>()
                for (i in grantResults.indices) {
                    val grantResult = grantResults[i]
                    val permission = permissions[i]
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        deniedPermissions.add(permission)
                    }
                }
                if (deniedPermissions.isEmpty()) {
                    mListener?.onGranted()
                } else {
                    mListener?.onDenied(deniedPermissions)
                }
            }
            else -> {
            }
        }
    }


}