package com.hxzk.main.ui.integral

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.hxzk.base.extension.sToast
import com.hxzk.main.R
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.base.BaseActivity
import com.hxzk.main.ui.mine.MineViewModel
import kotlinx.android.synthetic.main.activity_integral.*

class IntegralActivity : BaseActivity() {

    val viewModel by viewModels<IntegraListModel> { getViewModelFactory()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_integral)
    }
    override fun setupViews() {
       setupToolbar()
       title=resources.getString(R.string.interal_title)
        tv_iconCount.runWithAnimation(333f)
    }


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