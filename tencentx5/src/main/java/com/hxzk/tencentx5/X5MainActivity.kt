package com.hxzk.tencentx5

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hxzk.network.model.CommonItemModel
import kotlinx.android.synthetic.main.activity_x5main.*

class X5MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_x5main)
        val  model =intent.getParcelableExtra<CommonItemModel>(KEY_ITEMBEAN)
        x5WebView.loadWebUrl(model.link)
    }


    companion object{
        const val  KEY_ITEMBEAN = "key_itembean"
    }
}