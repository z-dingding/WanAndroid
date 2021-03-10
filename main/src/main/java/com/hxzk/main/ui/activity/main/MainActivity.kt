package com.hxzk.main.ui.activity.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.hxzk.main.R

import com.hxzk.main.ui.activity.base.BaseActivity


class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun setupViews(){}

    companion object{
        fun actionStart(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
    }

}