package com.hxzk.main.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hxzk.main.data.source.Repository
import com.hxzk.network.Result

/**
 *作者：created by zjt on 2021/4/2
 *描述:
 *
 */
class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val _banners : LiveData<Result<*>> = repository.banner()
    val banners : LiveData<Result<*>> =_banners



}