package com.hxzk.main.ui.system.sysitem

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.hxzk.base.extension.actionBundle
import com.hxzk.main.R
import com.hxzk.main.common.Const
import com.hxzk.main.databinding.ActivitySystemItemBinding
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.adapter.AnswerAdapter
import com.hxzk.main.ui.adapter.AnswerItemListAdapter
import com.hxzk.main.ui.base.BaseActivity
import com.hxzk.main.ui.x5Webview.X5MainActivity
import kotlin.properties.Delegates

class SystemItemActivity : BaseActivity(){

   private var  id by Delegates.notNull<Int>()
    val viewmodel by viewModels<SystemItemViewModel> { getViewModelFactory() }
    private lateinit var binding : ActivitySystemItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_system_item)
        binding.viewModel = viewmodel
        binding.lifecycleOwner = this
        intent.extras?.let {
            title=it.getString(Const.SystemItem.KEY_TITLE)
            id =it.getInt(Const.SystemItem.KEY_ID)
            viewmodel.cIdLiveData.value = id
        }
        setupToolbar()
        initAdapter()
        listener()
    }

    private fun listener() {
        viewmodel.itemClick.observe(this){
            val mBundle =Bundle()
            mBundle.putParcelable(X5MainActivity.KEY_ITEMBEAN,it)
            actionBundle<X5MainActivity>(this,mBundle)
        }
    }

    private fun initAdapter() {
        val adapter = AnswerItemListAdapter(viewmodel)
        binding.recycler.adapter =adapter
    }

}