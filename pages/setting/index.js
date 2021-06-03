// pages/setting/index.js

const app = getApp()
const wxapi = require('../wxapi/main')
/**
 * 储存收藏文章的key
 */
const key_collectionId = 'collectionid'
Page({

      /**
       * 页面的初始数据
       */
      data: {
        loading: false,
        version: '',
        currentSize: '',
        limitSize: '',
      },

      /**
       * 生命周期函数--监听页面加载
       */
      onLoad: function (options) {
        //获取版本号相关信息
        let version = this.getVersionInfo();
        //获取储存信息
        const res = wx.getStorageInfoSync();

        this.setData({
          version: version,
          currentSize: res.currentSize + 'KB',
          limitSize: res.limitSize + 'KB',
        })
      },

      /**
       * 生命周期函数--监听页面初次渲染完成
       */
      onReady: function () {

      },

      /**
       * 生命周期函数--监听页面显示
       */
      onShow: function () {

      },

      /**
       * 生命周期函数--监听页面隐藏
       */
      onHide: function () {

      },

      /**
       * 生命周期函数--监听页面卸载
       */
      onUnload: function () {

      },

      /**
       * 页面相关事件处理函数--监听用户下拉动作
       */
      onPullDownRefresh: function () {

      },

      /**
       * 页面上拉触底事件的处理函数
       */
      onReachBottom: function () {

      },

      /**
       * 用户点击右上角分享
       */
      onShareAppMessage: function () {

      },
      /**
       * 获取版本号相关信息
       */
      getVersionInfo() {

        console.log(wx.getAccountInfoSync())
        const accountInfo = wx.getAccountInfoSync();
        let version = '暂无信息'
        switch (accountInfo.miniProgram.envVersion) {
          case 'develop':
            version = '开发版';
            break
          case 'trial':
            version = '体验版';
            break
          case 'release':
            if(accountInfo.miniProgram.version != null && accountInfo.miniProgram.version != ''){
               version=accountInfo.miniProgram.version
            }else{
              version = '正式版';
            }
            break
        }
        return version;
      },
      /**
       *缓存大小点击
       */
      checkStorageSize: function () {
        wx.showModal({
          showCancel: false,
          title: '提示',
          content: '当前使用:' + this.data.currentSize + ',总计:' + this.data.limitSize + '.请放心使用.',
        })
      },
      checkVersion() {
        wx.showModal({
          showCancel: false,
          title: '提示',
          content: '小程序将自动更新,无需用户操作',
        })
      },
      /**
       * 退出登录的事件
       */
      loginout: function () {
        let _this = this;
        if (app.globalData.isLogin) {
          wx.showModal({
            title: '退出登录',
            content: '你确定要退出登录?',
            success(res) {
              if (res.confirm) {
                _this.setData({
                  loading: true
                })
                wxapi.loginOut().then((res) => {
                  if (res.data.errorCode == 0) {
                    _this.setData({
                      loading: false
                    })
                    //清除本地的cookie,收藏id,搜索记录等信息
                    wx.clearStorageSync()
                    //更新app全局状态
                    app.globalData.isLogin = false
                    wx.showToast({
                      title: '退出登录成功'
                    })
                  } else {
                    app.checkCodeDeal(res.data.errorCode, res.data.errorMsg)
                  }
                })
              } else if (res.cancel) {

              }
            }
          })
        } else {
          wx.showToast({
            title: '你已经退出登录了',
          })
        }
        }






        
      })



      