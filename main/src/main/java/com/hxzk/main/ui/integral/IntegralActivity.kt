package com.hxzk.main.ui.integral

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.hxzk.base.extension.action
import com.hxzk.base.extension.actionBundle
import com.hxzk.base.extension.sToast
import com.hxzk.base.util.GlobalUtil
import com.hxzk.main.R
import com.hxzk.main.common.Const
import com.hxzk.main.databinding.ActivityIntegralBinding
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.adapter.HomeItemAdapter
import com.hxzk.main.ui.adapter.IntegralItemAdapter
import com.hxzk.main.ui.base.BaseActivity
import androidx.lifecycle.observe
import com.hxzk.base.util.progressdialog.ProgressDialogUtil
import com.hxzk.main.ui.rank.RankActivity
import com.hxzk.main.ui.x5Webview.X5MainActivity
import com.hxzk.network.WanApi
import com.hxzk.network.model.CommonItemModel
import kotlinx.android.synthetic.main.activity_integral.*

class IntegralActivity : BaseActivity() {

    val viewModel by viewModels<IntegraViewModel> { getViewModelFactory()}
    lateinit var integralBinding:ActivityIntegralBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         integralBinding = DataBindingUtil.setContentView(this,R.layout.activity_integral)
         integralBinding.viewmodel = viewModel
         integralBinding.lifecycleOwner = this

        intent.extras?.let{
            val coinCount  = it.getInt(Const.IntegralList.KEY_COINCOUNT).toFloat()
            tv_iconCount.runWithAnimation(coinCount)
        }
        setupToolbar()
        title=resources.getString(R.string.interal_title)
        setupListAdapter()
        viewModel.requstData.value = true
        viewModel.dataLoading.observe(this){
            if (it) ProgressDialogUtil.getInstance().showDialog(activity)    else ProgressDialogUtil.getInstance().dismissDialog()
        }
    }



    /**
     * 设置RecyclerView的Adapter
     */
    private fun setupListAdapter() {
        val listAdapter = IntegralItemAdapter(viewModel)
        integralBinding.recycler.adapter = listAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_mine_integral,menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
             R.id.menuitem_integral_introduces-> {
                 // TODO: 2021/7/16 要防止积分说明被收藏
                 val model = CommonItemModel(123456,WanApi.baseUrl+GlobalUtil.getString(R.string.url_integral_instruction),GlobalUtil.getString(R.string.integral_instruction))
                 val mBundle =Bundle()
                 mBundle.putParcelable(X5MainActivity.KEY_ITEMBEAN,model)
                 actionBundle<X5MainActivity>(this,mBundle)

            }

            R.id.menuitem_integral_rank ->{ action<RankActivity>(this)}
        }
        return true
    }
}