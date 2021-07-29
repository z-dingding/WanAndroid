package com.hxzk.main.ui.collection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.hxzk.base.extension.actionBundle
import com.hxzk.base.util.progressdialog.ProgressDialogUtil
import com.hxzk.main.R
import com.hxzk.main.databinding.ActivityCollectionBinding
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.adapter.BrowseHistoryAdapter
import com.hxzk.main.ui.adapter.CollectionAdapter
import com.hxzk.main.ui.base.BaseActivity
import com.hxzk.main.ui.x5Webview.X5MainActivity
import com.hxzk.network.model.CommonItemModel
import com.hxzk.network.model.DataX

class CollectionActivity : BaseActivity() {
    val viewModel by viewModels<CollectionViewModel> { getViewModelFactory() }
    lateinit var binding : ActivityCollectionBinding
    private lateinit var listAdapter : CollectionAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_collection)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setupToolbar(resources.getString(R.string.collection_title))
        setupListAdapter()
        initObserve()
        smartListener()
        viewModel.requestData(true)
    }

    /**
     * 设置SmartRefreshLayout相关
     */
    private fun smartListener() {
        //刷新
        binding.smartRefresh.setOnRefreshListener {
            viewModel.requestData(true)
        }
        //加载更多
        binding.smartRefresh.setOnLoadMoreListener {
            viewModel.requestData(false)
        }

        viewModel.isRefreshing.observe(this) {
            if (!it){
                binding.smartRefresh.finishRefresh()
                //listAdapter.notifyDataSetChanged()
            }
        }
        viewModel.isLoadMoreing.observe(this){
            if (!it){
                binding.smartRefresh.finishLoadMore()
               // listAdapter.notifyDataSetChanged()
            }
        }

    }


    /**
     * 设置RecyclerView的Adapter
     */
    private fun setupListAdapter() {
        if (viewModel != null) {
            listAdapter = CollectionAdapter(viewModel)
            binding.recycler.adapter = listAdapter
            viewModel.itemClick.observe(this){
                //由于收藏列表没有collect字段,所以我们手动赋值collect为true
                val model = CommonItemModel(it.id,it.link,it.title,collect = true)
                val mBundle =Bundle()
                mBundle.putParcelable(X5MainActivity.KEY_ITEMBEAN,model)
                actionBundle<X5MainActivity>(this,mBundle)
            }
        }

    }

    private fun initObserve() {
        viewModel.unCollectionPos.observe(this){
            //先移除adapter中对应的索引,但不执行onBindViewHolder，也就是索引还存在
            listAdapter.notifyItemRemoved(it)
            //移除数据源中对应的索引
            viewModel.colItems.value?.removeAt(it)
            //对适配器重新绑定了一次数据,刷新当前索引到最后一个item索引的内容
            //会重新执行onBindViewHolder
            viewModel.colItems.value?.size?.let { it1 -> listAdapter.notifyItemRangeChanged(it, it1) }
        }
    }
}