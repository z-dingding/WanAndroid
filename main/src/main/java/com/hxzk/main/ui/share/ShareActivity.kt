package com.hxzk.main.ui.share

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.hxzk.base.extension.action
import com.hxzk.base.extension.actionForResult
import com.hxzk.base.extension.sToast
import com.hxzk.base.util.progressdialog.ProgressDialogUtil
import com.hxzk.main.R
import com.hxzk.main.common.Const.ShareActivity.Companion.KEY_REQUESTCODE
import com.hxzk.main.databinding.ActivityShareBinding
import com.hxzk.main.ui.adapter.RankAdapter
import com.hxzk.main.ui.adapter.ShareAdapter
import com.hxzk.main.ui.base.BaseActivity
import com.hxzk.main.ui.collection.CollectionActivity
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.WanApi
import com.hxzk.network.model.DataX
import com.hxzk.network.model.RankDataX
import kotlinx.android.synthetic.main.activity_rank.*
import kotlinx.coroutines.*
import java.lang.Exception
import androidx.lifecycle.observe
import com.hxzk.base.extension.actionBundle
import com.hxzk.base.util.Preference
import com.hxzk.main.common.Const
import com.hxzk.main.ui.x5Webview.X5MainActivity
import com.hxzk.network.model.CommonItemModel
import kotlin.properties.Delegates

class ShareActivity : BaseActivity() {

    private lateinit var databind: ActivityShareBinding

    /**
     * 总页码
     */
    var pageTotal = 1

    /**
     * 请求的页码，从1开始
     */
    var pageIndex = 1

    /**
     * 用户id
     */
    var userId by Delegates.notNull<Int>()

    /**
     * 创建协程作用域所需要的上下文
     */
    lateinit var scope: CoroutineScope

    /**
     * 数据源集合
     */
    lateinit var itemList: ArrayList<DataX>
    lateinit var adapter: ShareAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databind = DataBindingUtil.setContentView(this, R.layout.activity_share)
        initial()
    }

    private fun initial() {
        setupToolbar(resources.getString(R.string.title_share))
        val data by Preference(Const.ModifyUserInfo.KEY_USERID,0)
        userId = data
        val coroutContext = Dispatchers.IO + Job()
        scope = CoroutineScope(coroutContext)
        initSmartRefresh()
        initRecycler()
        showNoContentView()
        requstData(pageIndex)
    }


    private fun initSmartRefresh() {
        //刷新
        smartRefresh.setOnRefreshListener {
            pageIndex = 1
            itemList.clear()
            requstData(pageIndex)
        }
        //加载更多
        smartRefresh.setOnLoadMoreListener {
            pageIndex++
            requstData(pageIndex)
        }
    }

    private fun initRecycler() {
        itemList = ArrayList()
        adapter = ShareAdapter()
        recycler.adapter = adapter
        adapter.setItemClickListener(object : ShareAdapter.ItemClickListener{
            override fun itemClick(pos: Int, item: DataX) {
                val model = CommonItemModel(item.id, item.link, item.title,item.collect)
                val mBundle =Bundle()
                mBundle.putParcelable(X5MainActivity.KEY_ITEMBEAN,model)
              actionBundle<X5MainActivity>(this@ShareActivity,mBundle)
            }

        })
    }

    private fun requstData(pageIndex: Int) {
        if(pageIndex != 1 && pageIndex > pageTotal){
            smartRefresh.finishLoadMoreWithNoMoreData()
            return
        }
        if (pageIndex == 1) ProgressDialogUtil.getInstance().showDialog(activity)
        scope.launch {
            try {
                val result = WanApi.get().shareListArticle(pageIndex, userId)
                //切线程
                withContext(Dispatchers.Main) {
                    result.observe(this@ShareActivity) { it ->
                        if (it.errorCode == 0) {
                            hideAllStateView()
                            it.data?.shareArticles?.let {
                                pageTotal = it.pageCount
                                itemList.addAll(it.datas)
                                //submitList()更新现有列表,每次需要一个新的list
                                val newList = ArrayList<DataX>()
                                newList.addAll(itemList)
                                adapter.submitList(newList)
                            }
                        } else {
                            showLoadErrorView(it.errorMsg)
                        }
                        smartRefresh.finishLoadMore()
                        smartRefresh.finishRefresh()
                        if (pageIndex == 1) ProgressDialogUtil.getInstance().dismissDialog()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    if (pageIndex == 1) ProgressDialogUtil.getInstance().dismissDialog()
                    ResponseHandler.handleFailure(e)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == KEY_REQUESTCODE && resultCode == RESULT_OK) {
            //刷新数据列表
            pageIndex = 1
            itemList.clear()
            requstData(pageIndex)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.addShare -> actionForResult<CreateShareActivity>(this, KEY_REQUESTCODE)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}