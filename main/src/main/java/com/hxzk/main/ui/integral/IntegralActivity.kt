package com.hxzk.main.ui.integral

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.hxzk.base.extension.sToast
import com.hxzk.main.R
import com.hxzk.main.common.Const
import com.hxzk.main.databinding.ActivityIntegralBinding
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.adapter.HomeItemAdapter
import com.hxzk.main.ui.adapter.IntegralItemAdapter
import com.hxzk.main.ui.base.BaseActivity
import com.hxzk.main.ui.mine.MineViewModel
import kotlinx.android.synthetic.main.activity_integral.*

class IntegralActivity : BaseActivity() {

    val viewModel by viewModels<IntegraViewModel> { getViewModelFactory()}
    lateinit var integralBinding:ActivityIntegralBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         integralBinding = DataBindingUtil.setContentView(this,R.layout.activity_integral)
         integralBinding.viewmodel = viewModel
         integralBinding.lifecycleOwner = this

        val listAdapter  = IntegralItemAdapter(viewModel)
        integralBinding.recycler.adapter = listAdapter
    }


    override fun setupViews() {
        intent.extras?.let{
            val coinCount  = it.getInt(Const.IntegralList.KEY_COINCOUNT).toFloat()
            tv_iconCount.runWithAnimation(coinCount)
        }
       setupToolbar()
       title=resources.getString(R.string.interal_title)
        //setupListAdapter()
    }


//    /**
//     * 设置RecyclerView的Adapter
//     */
//    private fun setupListAdapter() {
//            val listAdapter = IntegralItemAdapter(viewModel)
//             recycler.adapter = listAdapter
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_mine_integral,menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
             R.id.menuitem_integral_introduces-> {
                getString(R.string.common_tips_wating).sToast()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}