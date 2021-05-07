package com.hxzk.main.ui.rank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.hxzk.base.extension.logDebug
import com.hxzk.base.extension.sToast
import com.hxzk.main.R
import com.hxzk.main.ui.adapter.RankAdapter
import com.hxzk.main.ui.base.BaseActivity
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.WanApi
import com.hxzk.network.model.RankDataX
import kotlinx.android.synthetic.main.activity_rank.*
import kotlinx.coroutines.*
import java.lang.Exception

class RankActivity : BaseActivity() {
    /**
     * 请求的页码，从1开始
     */
    var pageIndex =1

    /**
     * 创建协程作用域所需要的上下文
     */
    val coroutContext  = Dispatchers.IO+Job()
    lateinit var scope : CoroutineScope

    lateinit var  adapter : RankAdapter
    lateinit var  itemList:ArrayList<RankDataX>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_rank)
    }


    override fun setupViews() {
        setupToolbar()
        title=resources.getString(R.string.rank_title)
        scope = CoroutineScope(coroutContext)
        initSmartRefresh()
        initRecycler()
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
        adapter =RankAdapter()
        recycler.adapter =adapter
    }

    fun requstData(pageIndex:Int){
        scope.launch {
            try {
                val result =  WanApi.get().rankApi(pageIndex)
                //切线程
                withContext(Dispatchers.Main){
                if(result.errorCode == 0){
                    itemList.addAll(result.data?.datas!!)
                   adapter.submitList(itemList)
                }else{
                    result.errorMsg.sToast()
                }
                    smartRefresh.finishLoadMore()
                    smartRefresh.finishRefresh()
                }
            }catch (e:Exception){
                ResponseHandler.handleFailure(e)
            }

        }
    }




    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}