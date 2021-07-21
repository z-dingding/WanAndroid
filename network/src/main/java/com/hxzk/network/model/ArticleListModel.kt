package com.hxzk.network.model

/**
 *作者：created by zjt on 2021/4/13

 */

/**
 * 首页文章列表
 */
 class ArticleListModel(
    var curPage: Int,
    var datas: ArrayList<DataX>,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int
)

/**
 * 首页置顶文章列表
 */
data class DataX(
    // 区分item类型,0为内容item,1为标题item
        var itemType : Int,
        val apkLink: String,
        val audit: Int,
        val author: String,
        val canEdit: Boolean,
        val chapterId: Int,
        val chapterName: String,
     //是否收藏
        var collect: Boolean,
        val courseId: Int,
        val desc: String,
        val descMd: String,
        val envelopePic: String,
        val fresh: Boolean,
        val host: String,
        val id: Int,
        val link: String,
        val niceDate: String,
        val niceShareDate: String,
        val origin: String,
        val originId: Int,
        val prefix: String,
        val projectLink: String,
        val publishTime: Long,
        val realSuperChapterId: Int,
        val selfVisible: Int,
        val shareDate: Long,
        val shareUser: String,
        val superChapterId: Int,
        val superChapterName: String,
        val tags: List<Tag>,
        val title: String,
        val type: Int,
        val userId: Int,
        val visible: Int,
        val zan: Int
)

data class Tag(
    val name: String,
    val url: String
)

