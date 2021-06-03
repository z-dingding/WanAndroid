// index.js
// 获取应用实例
const app = getApp()
const wxapi = require("../wxapi/main")

Page({
  data: {
    //请求接口，页码从0开始
    pageIndex: 0,
    //当前页，从1开始
    curPage: 1,
    //总页数
    pageCount: 0,
    banners: [],
    //首页置顶数据
    topArticalList: [],
  },
  onLoad() {
    let _this = this
    //请求首页banner
    wxapi.banner().then(res => {
      if (res.data.errorCode == 0) {
        _this.setData({
          "banners": res.data.data
        })
      } else {
        app.checkCodeDeal(res.data.errorCode, res.data.errorMsg)
      }
    })
    //请求首页列表数据
    this.refershEvent();
    //检查是否有可更新的安装包(微信自动检测下载)
    this.checkUpdate();
  },
  /**
   * 重写页面下拉方法
   */
  onPullDownRefresh() {
    this.refershEvent();
  },
  /**
   * 重写页面上拉方法
   */
  onReachBottom() {
    this.loadMoreData();
  },
  /**
   * 点击搜索功能
   */
  search(e) {
    wx.navigateTo({
      url: '../homesearch/homesearch',
    })
  },
  /**
   * 点击首页列表项事件(注意,方法名不能和其他页面重复)
   * data-xxx小写获取
   */
  itemClick: function (e) {
    wx.navigateTo({
      url: '../webview/webview?type=1&urlPath=' + e.currentTarget.dataset.url + '&title=' + e.currentTarget.dataset.title + '&articalId=' + e.currentTarget.dataset.articalid
    })
  },


  /**
   *item中的tag点击（本站发布按钮的点击）
   */
  tagClick:function(e){
    let tag = e.currentTarget.dataset.tag
     wx.showToast({
       title: tag
      })
  },

  /**
   * banner的i的点击
   */
  bannerItemClick: function (e) {
    wx.navigateTo({
      url: '../webview/webview?type=1&urlPath=' + e.currentTarget.dataset.url + '&title=' + e.currentTarget.dataset.title + '&articalId=' + e.currentTarget.dataset.articalid+'&isShowCollection=false',
    })
  },
  /**
   * 刷新请求
   */
  refershEvent() {
    let _this = this
    //请求首页置顶数据
    wxapi.topArtical().then(function (res) {
      if (res.data.errorCode == 0) {
        _this.setData({
          "topArticalList": res.data.data
        })
        //紧接着请求首页列表数据
        wxapi.homeArticalList(_this.data.pageIndex).then(res => {
          if (res.data.errorCode == 0) {
            let array = _this.data.topArticalList;
            _this.setData({
              curPage: res.data.data.curPage,
              pageCount: res.data.data.pageCount,
              //concat返回新数组，不改变原数组
              topArticalList: array.concat(res.data.data.datas),
            })
            //停止刷新
            wx.stopPullDownRefresh();
          } else {
            app.checkCodeDeal(res.data.errorCode, res.data.errorMsg)
          }
        })
      } else {
        app.checkCodeDeal(res.data.errorCode, res.data.errorMsg)
      }
    })
  },
  /**
   * 加载更多请求
   */
  loadMoreData: function () {
    let _this =this;
    let page = ++this.data.pageIndex
    if (this.data.curPage < this.data.pageCount) {
      wxapi.homeArticalList(page).then(res => {
        if (res.data.errorCode == 0) {
          let array = _this.data.topArticalList;
          _this.setData({
            pageIndex: page,
            curPage: res.data.data.curPage,
            totalCount: res.data.data.totalCount,
            //concat返回新数组，不改变原数组
            topArticalList: array.concat(res.data.data.datas),
          })
        } else {
          app.checkCodeDeal(res.data.errorCode, res.data.errorMsg)
        }
      })
    }else {
      wx.showModal({
        showCancel: false,
        title:'提示',
        content:'已是最后一页!'
      })
    }
  },
  /**
   * 更新小程序
   */
  checkUpdate :function(){
    const updateManager = wx.getUpdateManager();
    updateManager.onUpdateReady(function () {
      wx.showModal({
        title: '更新提示',
        content: '新版本已经准备好，是否重启应用？',
        success: function (res) {
          if (res.confirm) {
            // 强制小程序重启并使用新版本
            updateManager.applyUpdate()
          }
        }
      })
    })
  }
})