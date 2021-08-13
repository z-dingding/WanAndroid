package com.hxzk.main.event

/**
 * @author: hxzk_zjt
 * @date: 2021/8/13
 * 描述:定义清除我的页面用户信息的Event事件
 *
 */
class ClearUserMessage constructor(val  isClear : Boolean) : MessageEvent() {
}