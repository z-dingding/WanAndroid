package com.hxzk.main.ui.answer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hxzk.base.extension.actionBundle
import com.hxzk.main.R
import com.hxzk.main.databinding.FragmentAnswerBinding
import com.hxzk.main.databinding.FragmentHomeBinding
import com.hxzk.main.event.TransparentStatusBarEvent
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.adapter.AnswerAdapter
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.main.ui.main.MainActivity
import com.hxzk.main.ui.x5Webview.X5MainActivity
import org.greenrobot.eventbus.EventBus


class AnswerFragment : BaseFragment() {

    val answerViewModel by viewModels<AnswerViewModel> { getViewModelFactory()}
    lateinit var  answerBinding : FragmentAnswerBinding
    lateinit var activity: MainActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        answerBinding = FragmentAnswerBinding.inflate(inflater, container, false).apply {
            this.viewModel = answerViewModel
        }
        setHasOptionsMenu(true)
        return super.onCreateView(answerBinding.root)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = requireActivity() as  MainActivity
        answerBinding.lifecycleOwner = this.viewLifecycleOwner
        initToolbar()
        initAdapter()
        initObserve()
        smartListener()
        answerViewModel.requestData(true)
    }

    override fun onResume() {
        super.onResume()
        val message = TransparentStatusBarEvent(false)
        EventBus.getDefault().post(message)
    }

    private fun smartListener() {
        //刷新
        answerBinding.smartRefresh.setOnRefreshListener {
            answerViewModel.pageIndex = 1;
            answerViewModel.requestData(true)
        }
        //加载更多
        answerBinding.smartRefresh.setOnLoadMoreListener {
            answerViewModel.pageIndex++
            answerViewModel.requestData(false)
        }
    }

    private fun initObserve() {
        answerViewModel.itemList.observe(viewLifecycleOwner){
            answerBinding.smartRefresh.finishRefresh()
            answerBinding.smartRefresh.finishLoadMore()
        }
        answerViewModel.itemClick.observe(viewLifecycleOwner){
            val mBundle =Bundle()
            mBundle.putParcelable(X5MainActivity.KEY_ITEMBEAN,it)
            activity.actionBundle<X5MainActivity>(activity,mBundle)
        }

    }

    private fun initAdapter() {
        val adapter = AnswerAdapter(answerViewModel)
        answerBinding.recycler.adapter =adapter
    }


    /**
     * 设置toolbar
     */
    private fun initToolbar() {
        //如果想要TextView居中那么就不要添加toolbar支持actonbar属性了
        //activity.setSupportActionBar(answerBinding.toolbar)
         val actionBar = activity.supportActionBar
        //隐藏左侧图标
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }

}