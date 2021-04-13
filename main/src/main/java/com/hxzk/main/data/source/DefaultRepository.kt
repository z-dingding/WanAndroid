package com.hxzk.main.data.source

import androidx.lifecycle.liveData
import com.hxzk.base.extension.sToast
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.Result
import com.hxzk.network.model.ApiResponse
import com.hxzk.network.model.ArticleListModel
import com.hxzk.network.model.DataX
import com.hxzk.network.succeeded
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 *作者：created by zjt on 2021/3/11
 *描述:统一调用入口
 *
 */
class DefaultRepository(
    private val localSource: DataSource,
    private val romtat: DataSource,
    private val ioDispatch: CoroutineDispatcher = Dispatchers.IO
) : Repository {

    //liveData是livedata-ktx的新技巧，自动构建并返回一个livedata对象
    //在代码块中提供一个挂起函数的上下文，这样我们在代码块中可以调用任意的挂起函数
    override fun login(account: String, pwd: String) = liveData(ioDispatch) {
        val result = romtat.login(account, pwd)
        emit(result)
    }

    override fun registerRequest(username: String, password: String, repassword: String) =
        liveData(ioDispatch) {
            val result = romtat.register(username, password, repassword)
            emit(result)
        }

    override fun banner() = liveData {
        val result = romtat.banner()
        emit(result)
    }


    override suspend fun articleList(pageIndex: Int): ArticleListModel =
        withContext(ioDispatch) {
            //首页列表
            val result = async { romtat.articleList(pageIndex) }
            //首页置顶
            val result2 =  async{romtat.topArticle()}


            lateinit var articleListModel: ArticleListModel

            val res2= result2.await()
            if (res2.succeeded) {
                val bean = (res2 as Result.Success<*>).res as ApiResponse<*>
                if (bean.errorCode == 0) {
                    val list  = bean.data  as ArrayList<DataX>
                    articleListModel = ArticleListModel(0,list,0,false,0,0,0)
                } else {
                    bean.errorMsg.sToast()
                }
            } else {
                ResponseHandler.handleFailure((res2 as Result.Error).e)
            }


            val res1 = result.await()
            if (res1.succeeded) {
                val bean = (res1 as Result.Success<*>).res as ApiResponse<*>
                if (bean.errorCode == 0) {
                    val model  = bean.data  as ArticleListModel
                    articleListModel.curPage =model.curPage
                    articleListModel.offset =model.offset
                    articleListModel.over =model.over
                    articleListModel.pageCount =model.pageCount
                    articleListModel.size =model.size
                    articleListModel.total =model.total
                    //两个集合相加
                    //articleListModel.datas.plus(model.datas)
                    model.datas.forEach {
                        articleListModel.datas.add(it)
                    }
                } else {
                    bean.errorMsg.sToast()
                }
            } else {
                ResponseHandler.handleFailure((res1 as Result.Error).e)
            }
            articleListModel
        }
}