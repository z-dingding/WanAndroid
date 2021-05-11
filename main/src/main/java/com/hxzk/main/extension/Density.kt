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

package com.hxzk.base.extension
import android.content.Context
import com.hxzk.base.util.Common



/**
 * 单位转换工具类，会根据手机的分辨率来进行单位转换。
 *
 * @author guolin
 * @since 2018/10/2
 */

/**
 * 根据手机的分辨率将dp转成为px
 */
fun Context.dpToPixel(dp: Int): Int {
    val displayMetrics = this.resources.displayMetrics
    //Math.round()四舍五入的原理是在参数上加0.5然后进行下取整。
    return if (dp < 0) dp else Math.round(dp * displayMetrics.density)
}


/**
 * 根据手机的分辨率将px转成dp
 */
fun Context.pixelToDp(pixel: Int): Int {
    val displayMetrics = this.resources.displayMetrics
    return if (pixel < 0) pixel else Math.round(pixel / displayMetrics.density)
}
