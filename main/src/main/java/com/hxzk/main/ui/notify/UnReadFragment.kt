package com.hxzk.main.ui.notify

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hxzk.base.extension.actionBundle
import com.hxzk.base.extension.sToast
import com.hxzk.base.util.progressdialog.ProgressDialogUtil
import com.hxzk.main.databinding.FragmentReadBinding
import com.hxzk.main.databinding.FragmentUnreadBinding
import com.hxzk.main.event.RegisterSuccessEvent
import com.hxzk.main.event.UnReadNumEvent
import com.hxzk.main.ui.adapter.NotifyReadAdapter
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.main.ui.x5Webview.X5MainActivity
import com.hxzk.main.util.ResponseHandler
import com.hxzk.main.widget.OnRecyclerItemClickListener
import com.hxzk.network.WanApi
import com.hxzk.network.model.CommonItemModel
import com.hxzk.network.model.DataX
import com.hxzk.network.model.DataX2
import kotlinx.android.synthetic.main.activity_rank.*
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus

/**
 * @author: hxzk_zjt
 * @date: 2021/7/30
 * 描述:
 */
class UnReadFragment : BaseFragment() {

    lateinit var databind : FragmentUnreadBinding

    /**
     * 创建协程作用域所需要的上下文
     */
    lateinit var scope: CoroutineScope

    /**
     * 数据源集合
     */
    lateinit var itemList: ArrayList<DataX2>
    lateinit var adapter: NotifyReadAdapter


    /**
     * 总页码
     */
    var pageTotal = 1

    /**
     * 请求的页码，从1开始
     */
    var pageIndex = 1

    lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        databind = FragmentUnreadBinding.inflate(inflater,container,false)
        return super.onCreateView(databind.root)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext  =requireContext()
        initial()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    private fun initial() {
        val coroutContext = Dispatchers.IO + Job()
        scope = CoroutineScope(coroutContext)
        initSmartRefresh()
        initRecycler()
        requstData(pageIndex)
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
        adapter = NotifyReadAdapter(mContext, itemList)
        databind.recycler.layoutManager = LinearLayoutManager(mContext)
        databind.recycler.adapter = adapter
        //添加Android自带的分割线
        //databind.recycler.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
        databind.recycler.addOnItemTouchListener(object : OnRecyclerItemClickListener(databind.recycler) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                val position = vh.layoutPosition
                val item = itemList[position]
                val model = CommonItemModel(item.id, item.link, item.title,collect = false)
                val mBundle = Bundle()
                mBundle.putParcelable(X5MainActivity.KEY_ITEMBEAN, model)
                (mContext as NotifyActivity).actionBundle<X5MainActivity>(mContext, mBundle)
            }

            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {
            }
        })
    }


    private fun requstData(pageIndex: Int) {
        if (pageIndex != 1 && pageIndex > pageTotal) {
            smartRefresh.finishLoadMoreWithNoMoreData()
            return
        }
        if (pageIndex == 1) ProgressDialogUtil.getInstance().showDialog(mContext)
        scope.launch {
            try {
                val result = WanApi.get().unReadList(pageIndex)
                //切线程
                withContext(Dispatchers.Main) {
                    result.observe(viewLifecycleOwner) { it ->
                        if (it.errorCode == 0) {
                            it.data?.let {
                                if (it.datas.isNotEmpty()) {
                                    pageTotal = it.pageCount
                                    itemList.addAll(it.datas)
                                    adapter.notifyDataSetChanged()
                                } else {
                                    "暂无数据".sToast()
                                }
                            }
                            //将已经预览的通知发送给MinaFragment
                            val unReadEvent = UnReadNumEvent(0)
                            EventBus.getDefault().post(unReadEvent)
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

}