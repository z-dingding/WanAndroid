package com.hxzk.main.common

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.hxzk.main.data.source.Repository
import com.hxzk.main.ui.answer.AnswerViewModel
import com.hxzk.main.ui.browsehistroy.BrowseHistoryViewModel
import com.hxzk.main.ui.collection.CollectionViewModel
import com.hxzk.main.ui.home.HomeViewModel
import com.hxzk.main.ui.integral.IntegraViewModel
import com.hxzk.main.ui.login.LoginViewModel
import com.hxzk.main.ui.mine.MineViewModel
import com.hxzk.main.ui.publics.PublicViewModel
import com.hxzk.main.ui.register.RegisterViewModel
import com.hxzk.main.ui.search.SearchViewModel
import com.hxzk.main.ui.system.child_nav.ChildNavigationViewModel
import com.hxzk.main.ui.system.child_sys.ChildSystemViewModel
import com.hxzk.main.ui.system.SystemViewModel
import com.hxzk.main.ui.system.sysitem.SystemItemViewModel
import com.hxzk.main.ui.x5Webview.X5FragViewModel

/**
 *作者：created by zjt on 2021/3/12
 *描述:
 *
 */
class ViewModelFactory constructor(
    private  val repository: Repository,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner,defaultArgs){
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ) = with(modelClass){
         when{
            isAssignableFrom(LoginViewModel::class.java) ->
                 LoginViewModel(repository)

            isAssignableFrom(RegisterViewModel::class.java) ->
                RegisterViewModel(repository)

            isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(repository)

            isAssignableFrom(MineViewModel::class.java) ->
                MineViewModel(repository)

           isAssignableFrom(IntegraViewModel::class.java) ->
               IntegraViewModel(repository)

           isAssignableFrom(AnswerViewModel::class.java) ->
               AnswerViewModel(repository)

           isAssignableFrom(SystemViewModel::class.java) ->
               SystemViewModel(repository)

           isAssignableFrom(ChildSystemViewModel::class.java) ->
               ChildSystemViewModel(repository)

           isAssignableFrom(ChildNavigationViewModel::class.java) ->
               ChildNavigationViewModel(repository)

           isAssignableFrom(SearchViewModel::class.java) ->
               SearchViewModel(repository)

           isAssignableFrom(PublicViewModel::class.java) ->
               PublicViewModel(repository)

             isAssignableFrom(SystemItemViewModel::class.java) ->
                 SystemItemViewModel(repository)

             isAssignableFrom(X5FragViewModel::class.java) ->
                 X5FragViewModel(repository)
             isAssignableFrom(BrowseHistoryViewModel::class.java) ->
                 BrowseHistoryViewModel(repository)
             isAssignableFrom(CollectionViewModel::class.java) ->
                 CollectionViewModel(repository)
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}