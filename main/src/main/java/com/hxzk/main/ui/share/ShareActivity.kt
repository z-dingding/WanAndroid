package com.hxzk.main.ui.share

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hxzk.base.extension.actionBundle
import com.hxzk.base.extension.actionForResult
import com.hxzk.base.extension.sToast
import com.hxzk.base.util.Preference
import com.hxzk.base.util.progressdialog.ProgressDialogUtil
import com.hxzk.main.R
import com.hxzk.main.common.Const
import com.hxzk.main.common.Const.ShareActivity.Companion.KEY_REQUESTCODE
import com.hxzk.main.databinding.ActivityShareBinding
import com.hxzk.main.ui.adapter.ShareAdapter
import com.hxzk.main.ui.base.BaseActivity
import com.hxzk.main.ui.x5Webview.X5MainActivity
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.WanApi
import com.hxzk.network.model.CommonItemModel
import com.hxzk.network.model.DataX
import kotlinx.android.synthetic.main.activity_rank.*
import kotlinx.coroutines.*
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
    private var userId by Delegates.notNull<Int>()

    /**
     * 创建协程作用域所需要的上下文
     */
    lateinit var scope: CoroutineScope

    /**
     * 数据源集合
     */
    lateinit var itemList: ArrayList<DataX>
    lateinit var adapter: ShareAdapter

    /**
     * 记录上一个选中的checkBox所在item的索引,注意时isCheck为true的情况
     */
    var lastSelCheck = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databind = DataBindingUtil.setContentView(this, R.layout.activity_share)
        setContentView(databind.root)
        initial()
    }

    private fun initial() {
        setupToolbar(resources.getString(R.string.title_share))
        val data by Preference(Const.ModifyUserInfo.KEY_USERID, 0)
        userId = data
        val coroutContext = Dispatchers.IO + Job()
        scope = CoroutineScope(coroutContext)
        initSmartRefresh()
        initRecycler()
        showNoContentView()
        requstData(pageIndex)
        databind.sbDelShare.setOnClickListener {
            val id = itemList.get(lastSelCheck).id
            delShareArticle(id)
        }

    }


    private fun initSmartRefresh() {
        //刷新
        databind.smartRefresh.setOnRefreshListener {
            pageIndex = 1
            itemList.clear()
            requstData(pageIndex)
        }
        //加载更多
        databind.smartRefresh.setOnLoadMoreListener {
            pageIndex++
            requstData(pageIndex)
        }
    }

    private fun initRecycler() {
        itemList = ArrayList()
        adapter = ShareAdapter(this, itemList)
        databind.recycler.layoutManager = LinearLayoutManager(this)
        databind.recycler.adapter = adapter
        //添加Android自带的分割线
        databind.recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        adapter.setItemClickListener(object : ShareAdapter.ItemClickListener {
            override fun itemClick(pos: Int, item: DataX) {
                val model = CommonItemModel(item.id, item.link, item.title, item.collect)
                val mBundle = Bundle()
                mBundle.putParcelable(X5MainActivity.KEY_ITEMBEAN, model)
                actionBundle<X5MainActivity>(this@ShareActivity, mBundle)
            }

            override fun itemDelClick(pos: Int, isCheck: Boolean) {
                //更新当前操作checkbox的选中的状态
                itemList[pos].isDelShare = isCheck
                //如果选中当前,且和上一个选中还是同一个(重复点击一个的情况)
                if (isCheck && lastSelCheck == pos) {
                    return
                }
                //选中但不是同一个
                else if (isCheck && lastSelCheck != pos) {
                    //将上一个选中的状态改为未选中
                    if (lastSelCheck != -1) {
                        itemList[lastSelCheck].isDelShare = false
                        adapter.notifyItemChanged(lastSelCheck, R.id.cb_delitem);
                    }
                } else if (!isCheck && lastSelCheck == pos) {
                    //取消上一个列表项cb选中状态
                    lastSelCheck = -1
                }
                //记录选中状态的上一个索引值
                if (isCheck) lastSelCheck = pos

                if (lastSelCheck != -1) {
                    //当前有选中的item
                    databind.sbDelShare.isEnabled = true
                    databind.sbDelShare.setBackgroundResource(R.color.colorPrimary)
                } else {
                    databind.sbDelShare.isEnabled = false
                    databind.sbDelShare.setBackgroundResource(R.color.text_gray2)
                }
            }

        })
    }

    private fun requstData(pageIndex: Int) {
        if (pageIndex != 1 && pageIndex > pageTotal) {
            smartRefresh.finishLoadMoreWithNoMoreData()
            return
        }
        if (pageIndex == 1) ProgressDialogUtil.getInstance().showDialog(this)
        scope.launch {
            try {
                val result = WanApi.get().shareListArticle(pageIndex, userId)
                //切线程
                withContext(Dispatchers.Main) {
                    result.observe(this@ShareActivity) { it ->
                        if (it.errorCode == 0) {

                            it.data?.shareArticles?.let {
                                if(it.datas.isNotEmpty()){
                                    pageTotal = it.pageCount
                                    itemList.addAll(it.datas)
                                    adapter.notifyDataSetChanged()
                                    hideAllStateView()
                                }else{
                                   "暂无数据".sToast()
                                }

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


    private fun delShareArticle(id: Int) {
        ProgressDialogUtil.getInstance().showDialog(this)
        scope.launch {
            try {
                val result = WanApi.get().delSharedArticle(id)
                //切线程
                withContext(Dispatchers.Main) {
                    if (result.errorCode == 0) {
                        adapter.notifyItemRemoved(lastSelCheck)
                        itemList.removeAt(lastSelCheck)
                        //1 是起始位置，从哪里开始更新，2 更新的总数
                        adapter.notifyItemRangeChanged(lastSelCheck, itemList.size)
                        lastSelCheck = -1
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == KEY_REQUESTCODE && resultCode == RESULT_OK) {
            //当前选中的cb重置
            lastSelCheck = -1
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