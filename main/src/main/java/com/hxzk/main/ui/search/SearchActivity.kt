package com.hxzk.main.ui.search

import android.annotation.TargetApi
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.transition.Transition
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.core.os.postDelayed
import androidx.databinding.DataBindingUtil
import com.google.android.flexbox.FlexboxLayoutManager
import com.hxzk.base.extension.logWarn
import com.hxzk.base.extension.sToast
import com.hxzk.base.util.AndroidVersion
import com.hxzk.base.util.Common
import com.hxzk.main.R
import com.hxzk.main.databinding.ActivitySearchBinding
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.adapter.FlexItemAdapter
import com.hxzk.main.ui.base.BaseActivity
import com.quxianggif.common.transitions.TransitionUtils
import androidx.lifecycle.observe
import com.hxzk.base.extension.dpToPixel
import com.hxzk.main.callback.HotFlexItemClickListener
import com.hxzk.network.model.HotKeyModel

class SearchActivity : BaseActivity() {

    val viewModel by viewModels<SearchViewModel> { getViewModelFactory() }
    lateinit var binding : ActivitySearchBinding
    private lateinit var adapter: FlexItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun onStart() {
        super.onStart()
        setupSearchView()
        setupTransitions()
        setRecycler()
        liveDataObserve()
    }

    private fun liveDataObserve() {
        viewModel.hotKeys.observe(this){
            for (index in it.iterator()){
                var lp = FlexboxLayoutManager.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
                val end = dpToPixel(15)
                val top = dpToPixel(10)
         lp.setMargins(0,0,end,top)
                adapter.addItem(lp)
            }
        }
    }

    private fun setRecycler() {
        val flexboxLayoutManager = FlexboxLayoutManager(activity)
        binding.hotResults.layoutManager = flexboxLayoutManager
        adapter = FlexItemAdapter(viewModel,this)
        binding.hotResults.adapter = adapter
        adapter.setFlexItemClickListener(object : HotFlexItemClickListener {
            override fun onItemClick(item: HotKeyModel) {
                item.name.sToast()
            }
        })
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
              query.sToast()
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (TextUtils.isEmpty(query)) {
                    //清空输入内容
                }
                return true
            }
        })

        binding.searchBack.setOnClickListener {
            dismiss()
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