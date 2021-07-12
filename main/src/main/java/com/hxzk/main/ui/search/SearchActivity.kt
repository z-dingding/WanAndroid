package com.hxzk.main.ui.search

import android.annotation.TargetApi
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.transition.Transition
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.core.os.postDelayed
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.google.android.flexbox.FlexboxLayoutManager
import com.hxzk.base.extension.logWarn
import com.hxzk.base.util.AndroidVersion
import com.hxzk.base.util.Common
import com.hxzk.main.R
import com.hxzk.main.databinding.ActivitySearchBinding
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.adapter.FlexItemAdapter
import com.hxzk.main.ui.base.BaseActivity
import com.quxianggif.common.transitions.TransitionUtils
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.hxzk.base.extension.actionBundle
import com.hxzk.base.extension.dpToPixel
import com.hxzk.main.callback.HotFlexItemClickListener
import com.hxzk.main.callback.SearchFlexItemClickListener
import com.hxzk.main.ui.adapter.AnswerAdapter
import com.hxzk.main.ui.adapter.SearchResultAdapter
import com.hxzk.main.ui.x5Webview.X5MainActivity
import com.hxzk.network.model.DataX
import com.hxzk.network.model.HotKeyModel
import com.hxzk.network.model.SearchKeyWord
import kotlin.concurrent.thread

class SearchActivity : BaseActivity() {

    val viewModel by viewModels<SearchViewModel> { getViewModelFactory() }
    lateinit var binding : ActivitySearchBinding
    private lateinit var adapter: FlexItemAdapter
    private lateinit var keyWordAdapter: FlexItemAdapter
    private lateinit var searchAdapter : SearchResultAdapter

    /**
     * 热词搜索是否是首次进入
     */
    var isFirstShow : Boolean = true

    /**
     * 历史搜索是否是首次进入
     */
    var isSearchFirstShow : Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setupSearchView()
        setupTransitions()
        setRecycler()
        //先本地查询热词数据,没有就网络获取
        thread {
            val localData = viewModel.queryKeywords()
            if (localData != null && localData.isNotEmpty()) {
                var liveData = MutableLiveData(localData)
                viewModel.hotKeys =liveData
                runOnUiThread {
                    addItemToAdapter(localData.size,isFirstShow)
                }
            }else{
                runOnUiThread {
                    viewModel.hotKeys.observe(this){
                        addItemToAdapter(it.size,isFirstShow)
                    }
                }
            }
        }
        //查询本地搜索记录
        viewModel.searchKeyword.observe(this){
            if(it.isNotEmpty()){
                addSearchItemToAdapter(it.size,isSearchFirstShow)
            }else{
                keyWordAdapter.removeAllItem()
            }
        }
        binding.tvClearHistory.setOnClickListener {
            viewModel.clearAllSearchKeys()
        }
    }


    /**
     *  将请求的热词结果,添加到adatepr并刷新
     */
    private fun addItemToAdapter(list: Int,isFirstShow :Boolean){
        if(isFirstShow){
            //首次进入页面,循环遍历
            for( index in 0 until list){
                var lp = generateFlexLayoutParams()
                adapter.addItem(lp)
            }
            this.isFirstShow =false
        }else{
             //除首次进入以外,动态添加,一个个加入
            var lp = generateFlexLayoutParams()
                //热词搜索
            adapter.addItem(lp)
        }

   }
    /**
     *  将本地搜索历史结果,添加到adatepr并刷新
     */
    private fun addSearchItemToAdapter(list: Int,isSearchFirstShow :Boolean){
        if(isSearchFirstShow){
            //首次进入页面,循环遍历
            for( index in 0 until list){
                var lp = generateFlexLayoutParams()
                    //历史搜索
                    keyWordAdapter.addItem(lp)
            }
            this.isSearchFirstShow =false
        }else{
            //动态添加,一个个加入
            var lp = generateFlexLayoutParams()
                //历史搜索
                keyWordAdapter.addItem(lp)
        }

    }

    /**
     * 生成adapter的每个item对应的LayoutParams
     */
    private fun generateFlexLayoutParams() :FlexboxLayoutManager.LayoutParams {
        var lp = FlexboxLayoutManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        val right = dpToPixel(15)
        val bottom = dpToPixel(10)
        lp.setMargins(0,0,right,bottom)

        return lp
    }

    private fun setRecycler() {
        val flexboxLayoutManager = FlexboxLayoutManager(activity)
        val flexboxLayoutManager2 = FlexboxLayoutManager(activity)

        binding.hotResults.layoutManager = flexboxLayoutManager
        binding.searchKeysRv.layoutManager = flexboxLayoutManager2

        adapter = FlexItemAdapter(viewModel,true)
        keyWordAdapter = FlexItemAdapter(viewModel,false)

        binding.hotResults.adapter = adapter
        binding.searchKeysRv.adapter = keyWordAdapter

        adapter.setFlexItemClickListener(object : HotFlexItemClickListener {
            override fun onItemClick(item: HotKeyModel) {
                binding.searchView.setQuery(item.name,false)
                searchDataBykey(item.name,true)
            }
        })

        keyWordAdapter.setSearchFexItemClickListener(object : SearchFlexItemClickListener {
            override fun onItemClick(item: SearchKeyWord) {
                binding.searchView.setQuery(item.searchKey,false)
                searchDataBykey(item.searchKey,false)
            }

        })
        //设置搜索请求结果的Rv
         val linearLayout = LinearLayoutManager(this)
        binding.rvSearchResult.layoutManager = linearLayout
         searchAdapter = SearchResultAdapter(viewModel)
        binding.rvSearchResult.adapter= searchAdapter
        viewModel.itemClick.observe(this){
            val mBundle =Bundle()
            mBundle.putParcelable(X5MainActivity.KEY_ITEMBEAN,it)
            actionBundle<X5MainActivity>(this,mBundle)
        }
    }

    @TargetApi(22)
    private fun setupTransitions() {
        if (AndroidVersion.hasLollipopMR1()) {
            window.enterTransition.addListener(object : TransitionUtils.TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition) {
                    binding.searchView.requestFocus()
                    showKeyboard()
                }
            })
        } else {
            binding.searchView.requestFocus()
            Common.getMainHandler().postDelayed(100){
                showKeyboard()
            }
        }
    }

    private fun setupSearchView() {
        binding.searchView.queryHint = getString(R.string.search_hint)
        binding.searchView.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
        binding.searchView.imeOptions = binding.searchView.imeOptions or EditorInfo.IME_ACTION_SEARCH or
                EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_FLAG_NO_FULLSCREEN
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchDataBykey(query,false)
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (TextUtils.isEmpty(query)) {
                    //清空输入内容
                    binding.searchKeysRv.visibility = View.VISIBLE
                    binding.searchkeyRL.visibility = View.VISIBLE
                    binding.hotResults.visibility =View.VISIBLE
                    binding.hotkeyLL.visibility = View.VISIBLE

                    binding.rvSearchResult.visibility =View.GONE
                }
                return true
            }
        })

        binding.searchBack.setOnClickListener {
            dismiss()
        }
    }

    /**
     * 执行搜索请求
     */
    private fun searchDataBykey(keyWord : String ,isHotKey : Boolean) {
        if (viewModel.searchKeyword.value.isNullOrEmpty()) {
            //如果没有历史搜索数据
            if (!isHotKey) {
                //如果不是热词搜索存入数据库
                val item = SearchKeyWord(keyWord)
                viewModel.insertSearchKey(item)
            }
        } else {
            val ele = SearchKeyWord(keyWord)
            if (!isHotKey && viewModel.searchKeyword.value!!.indexOf(ele) == -1) {
                //如果不是热词搜索且历史搜索中不存在就存入数据库
                val item = SearchKeyWord(keyWord)
                viewModel.insertSearchKey(item)
            }
        }
        //执行搜索请求
        viewModel.searchDataBykey(keyWord, 0).observe(this) {
            binding.searchKeysRv.visibility = View.GONE
            binding.searchkeyRL.visibility = View.GONE
            binding.hotResults.visibility = View.GONE
            binding.hotkeyLL.visibility = View.GONE

            binding.rvSearchResult.visibility = View.VISIBLE
            searchAdapter.submitList(it)
            //搜索完,隐藏软键盘
            hideKeyboard()
        }
    }

    private fun dismiss() {
        //清除背景,否则返回上一页面动画会重叠，观感差
        binding.searchBack.background = null
        if (AndroidVersion.hasLollipop()) {
            finishAfterTransition()
        } else {
            finish()
        }
    }

    private fun showKeyboard() {
        try {
            val id = binding.searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
            val editText = binding.searchView.findViewById<EditText>(id)
            showSoftKeyboard(editText)
        } catch (e: Exception) {
            logWarn( e.message, e)
        }
    }

    private fun hideKeyboard() {
        binding.searchView.clearFocus()
        hideSoftKeyboard()
    }





}