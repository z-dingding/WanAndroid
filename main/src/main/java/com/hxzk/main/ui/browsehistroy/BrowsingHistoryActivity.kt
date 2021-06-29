package com.hxzk.main.ui.browsehistroy

import android.os.Bundle
import androidx.lifecycle.observe
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.hxzk.base.util.GlobalUtil
import com.hxzk.main.R
import com.hxzk.main.databinding.ActivityBrowsingHistoryBinding
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.adapter.BrowseHistoryAdapter
import com.hxzk.main.ui.base.BaseActivity

class BrowsingHistoryActivity : BaseActivity() {

    val viewModel by viewModels<BrowseHistoryViewModel> { getViewModelFactory() }
    lateinit var binding: ActivityBrowsingHistoryBinding
    lateinit var listAdapter: BrowseHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_browsing_history)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initObserve()
    }

    private fun initObserve() {
        viewModel.items.observe(this){
            if(it.isNotEmpty()){
                hideAllStateView()
            }else{
                showNoContentView()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setupToolbar(resources.getString(R.string.title_browsehistory))
        setupListAdapter()
    }

    /**
     * 设置RecyclerView的Adapter
     */
    private fun setupListAdapter() {
        if (viewModel != null) {
            listAdapter = BrowseHistoryAdapter(viewModel)
            binding.recycler.adapter = listAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_browsehistory_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.clearAll -> showClearAllDialog()
        }
        return true
    }

    /**
     * 清空所有的弹窗
     */
    private fun showClearAllDialog() {
        val build = AlertDialog.Builder(this, R.style.AlertDialogStyle).apply {
            setTitle(resources.getString(R.string.title_browsehistory))
            setMessage(resources.getString(R.string.browsehistory_clearall_hint))
            setPositiveButton(GlobalUtil.getString(R.string.browsehistory_sure)) { _, _ ->
                run {
                    clearAllHistory()
                }
            }
            setNegativeButton(GlobalUtil.getString(R.string.browsehistory_cancel), null)
        }
        build.create().show()
    }

    /**
     * 执行清除所有的记录
     */
    private fun clearAllHistory() {
        viewModel.clearAllHistory()
        showNoContentView()
    }


}