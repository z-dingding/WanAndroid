package com.hxzk.main.ui.share

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.hxzk.base.extension.sToast
import com.hxzk.base.util.GlobalUtil
import com.hxzk.main.R
import com.hxzk.main.databinding.ActivityCreateShareBinding
import com.hxzk.main.ui.base.BaseActivity
import com.hxzk.network.WanApi
import com.hxzk.base.extension.sMainToast
import com.hxzk.base.util.Common
import com.hxzk.base.util.progressdialog.ProgressDialogUtil
import com.hxzk.main.common.MainApplication
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.model.ApiResponse
import com.hxzk.network.model.StringModel
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse
import java.lang.Exception

class CreateShareActivity : BaseActivity() {

    lateinit var databind: ActivityCreateShareBinding
    lateinit var coroutineScope: CoroutineScope


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databind = DataBindingUtil.setContentView(this, R.layout.activity_create_share)
        setupToolbar(resources.getString(R.string.title_createshare))
        //设置底部的说明
        setInstruction()
        val coroutineContext = Dispatchers.IO + Job()
        coroutineScope = CoroutineScope(coroutineContext)
        initEvent()
    }

    private fun initEvent() {
        databind.tvResetTitle.setOnClickListener {
            databind.etInputTitle.setText("")
        }
        databind.tvOpenLink.setOnClickListener {
            resources.getString(R.string.common_tips_pleasewaiting).sToast()
        }
        databind.sbShare.setOnClickListener {
            addArticle()
        }
    }

    /**
     * 请求添加分享
     */
    private fun addArticle() {
        if (databind.etInputTitle.text.isEmpty()) {
            resources.getString(R.string.createshare_notempty_articletitle).sToast()
            return
        }
        if (databind.etInputLink.text.isEmpty()) {
            resources.getString(R.string.createshare_notempty_articlelink).sToast()
            return
        }
        try {
            ProgressDialogUtil.getInstance().showDialog(activity)
            coroutineScope.launch {
                val result = WanApi.get().addShareArticle(databind.etInputTitle.text.toString(), databind.etInputLink.text.toString())
                withContext(Dispatchers.Main) {
                    if (result.errorCode == 0) {
                        setResult(RESULT_OK, intent)
                        finish()
                    } else {
                        result.errorMsg.sMainToast()
                    }
                    ProgressDialogUtil.getInstance().dismissDialog()
                }
            }
        } catch (e: Exception) {
            Common.getMainHandler().post {
                ProgressDialogUtil.getInstance().dismissDialog()
                ResponseHandler.handleFailure(e)
            }
        }
    }

    private fun setInstruction() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            databind.tvInstruction.text = Html.fromHtml("<ol><li>1.只要是任何好文都可以分享,并不一定要是原创!投递的文章都会进入广场Tab</li><li>2.CSDN,掘金，简书等官方博客站点会直接通过，不需要审核</li><li>3.其他个人站点会进入审核阶段，不要投递任何无效链接，否则可能会对你的账号产生影响</li><li>4.如果你发现错误，可以提交日志，让我们一起使网站变得更好。</li><li>5.由于本站为个人开发与维护，会尽力保证24小时内审核，当然有可能哪天太累，会延期，请保持佛系...</li></ol>", 0)
        } else {
            databind.tvInstruction.text = Html.fromHtml("<ol><li>1.只要是任何好文都可以分享,并不一定要是原创!投递的文章都会进入广场Tab</li><li>2.CSDN,掘金，简书等官方博客站点会直接通过，不需要审核</li><li>3.其他个人站点会进入审核阶段，不要投递任何无效链接，否则可能会对你的账号产生影响</li><li>4.如果你发现错误，可以提交日志，让我们一起使网站变得更好。</li><li>5.由于本站为个人开发与维护，会尽力保证24小时内审核，当然有可能哪天太累，会延期，请保持佛系...</li></ol>")
        }
    }


}