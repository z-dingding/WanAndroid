package com.hxzk.main.event

/**
 *作者：created by zjt on 2021/1/25
 *描述:
 *
 */
class FinishActivityEvent :MessageEvent() {
    var activityClass: Class<*>? = null
}