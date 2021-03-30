package com.hxzk.main.event

/**
 *作者：created by zjt on 2021/3/30
 *描述:
 *
 */
data class RegisterSuccessEvent constructor( val account: String, val  pwd : String) : MessageEvent()