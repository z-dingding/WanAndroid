package com.hxzk.main.ui.activity.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.hxzk.main.R
import com.hxzk.main.ui.activity.base.BaseActivity
import com.hxzk.main.ui.activity.login.LoginActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    override fun setupViews() {
        accaountLogin.setOnClickListener {
            LoginActivity.startAction(this)
        }
    }

    override fun onBackPressed() {
        LoginActivity.startAction(this)
    }

    companion object{

        fun  startAction(activity: Activity){
            val  intent = Intent(activity, RegisterActivity::class.java)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.rotate_start,R.anim.rotate_end)
        }
    }
}