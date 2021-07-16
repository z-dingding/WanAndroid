package com.hxzk.main.ui.collection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.hxzk.main.R
import com.hxzk.main.databinding.ActivityCollectionBinding
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.base.BaseActivity

class CollectionActivity : BaseActivity() {
    val viewModel by viewModels<CollectionViewModel> { getViewModelFactory() }
    lateinit var binding : ActivityCollectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_collection)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
       setupToolbar(resources.getString(R.string.collection_title))
    }
}