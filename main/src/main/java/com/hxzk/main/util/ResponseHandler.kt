/*
 * Copyright (C) guolin, Suzhou Quxiang Inc. Open source codes for study only.
 * Do not use for commercial purpose.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hxzk.main.util

import com.hxzk.base.extension.logWarn
import com.hxzk.base.extension.sMainToast
import com.hxzk.base.util.GlobalUtil
import com.hxzk.main.R
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 对服务器的返回进行相应的逻辑处理。注意此类只处理公众的返回逻辑，涉及具体的业务逻辑，仍然交由接口调用处自行处理。
 */
object ResponseHandler {

    private val TAG = "ResponseHandler"



    /**
     * 当网络请求没有正常响应的时候，根据异常类型进行相应的处理。
     * @param e
     * 异常实体类
     */
    fun handleFailure(e: Exception) {
        when (e) {
            is ConnectException -> GlobalUtil.getString(R.string.network_connect_error).sMainToast()
            is SocketTimeoutException -> GlobalUtil.getString(R.string.network_connect_timeout).sMainToast()
            is NoRouteToHostException -> GlobalUtil.getString(R.string.no_route_to_host).sMainToast()
            is UnknownHostException -> GlobalUtil.getString(R.string.network_no_host).sMainToast()
            else -> {
                logWarn(TAG, "handleFailure exception is $e")
                (GlobalUtil.getString(R.string.unknown_error)).sMainToast()
            }
        }
    }

}