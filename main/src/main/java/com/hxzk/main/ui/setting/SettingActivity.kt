package com.hxzk.main.ui.setting

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Secure.putString
import android.view.View
import androidx.databinding.DataBindingUtil
import com.hxzk.base.extension.sToast
import com.hxzk.base.util.GlobalUtil
import com.hxzk.base.util.Preference
import com.hxzk.base.util.progressdialog.ProgressDialogUtil
import com.hxzk.main.R
import com.hxzk.main.common.Const
import com.hxzk.main.databinding.ActivitySearchBinding
import com.hxzk.main.databinding.ActivitySettingBinding
import com.hxzk.main.generated.callback.OnClickListener
import com.hxzk.main.ui.base.BaseActivity
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.NetWork
import com.hxzk.network.WanApi
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SettingActivity : BaseActivity() , View.OnClickListener {

    lateinit var  binding: ActivitySettingBinding
    lateinit var scope:CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_setting)
        initData()
        initEvent()
    }

    private fun initData() {
        setupToolbar(resources.getString(R.string.mine_setting))
        binding.stvVersion.setRightString(GlobalUtil.appVersionName)
        val scopeContext = Dispatchers.IO + Job()
        scope = CoroutineScope(scopeContext)
    }

    private fun initEvent() {
        binding.stvVersion.setOnClickListener(this)
        binding.stvAuthor.setOnClickListener(this)
        binding.stvLoginout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
             R.id.stv_version -> resources.getString(R.string.setting_version_tips).sToast()
             R.id.stv_author -> resources.getString(R.string.common_tips_wating).sToast()
             R.id.stv_loginout -> loginOut()
            }
        }
    }

    private fun loginOut() {
        ProgressDialogUtil.getInstance().showDialog(this)
            scope.launch {
                try {
                    val result = WanApi.get().loginOut()
                    //切线程
                    withContext(Dispatchers.Main) {
                        if (result.errorCode == 0) {
                           resources.getString(R.string.logout_success).sToast()
                            //将登录信息重置
                        getSharedPreferences("share_prefer", Context.MODE_PRIVATE).edit().apply {
                            putString("key_account","")
                            putString("key_pwd","")
                            putString(NetWork.KEY_COOKIES,"")
                            apply()
                            }
                        } else {
                            result.errorMsg.sToast()
                        }
                        ProgressDialogUtil.getInstance().dismissDialog()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        ProgressDialogUtil.getInstance().dismissDialog()
                        ResponseHandler.handleFailure(e)
                    }
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}