package com.hxzk.main.util

import android.graphics.Bitmap
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import com.hxzk.main.util.ColorUtil.isDark

/**
 * @author: hxzk_zjt
 * @date: 2021/6/4
 * 描述:
 */
object ColorUtil {
    private const val IS_LIGHT = 0
    private const val IS_DARK = 1
    private const val LIGHTNESS_UNKNOWN = 2


    /**
     * 判断传入的图片的颜色属于深色还是浅色。
     * @param bitmap
     * 图片的Bitmap对象。
     * @return 返回true表示图片属于深色，返回false表示图片属于浅色。
     */
    fun isBitmapDark(palette: Palette?, bitmap: Bitmap): Boolean {
        val isDark: Boolean
        val lightness =isDark(palette)
        if (lightness == LIGHTNESS_UNKNOWN) {
            isDark = isDark(bitmap, bitmap.width / 2, 0)
        } else {
            isDark = lightness == IS_DARK
        }
        return isDark
    }

    fun isDark(palette: Palette?): Int {
        val mostPopulous = getMostPopulousSwatch(palette) ?: return LIGHTNESS_UNKNOWN
        return if (isDark(mostPopulous.hsl)) IS_DARK else IS_LIGHT
    }

    fun isDark(bitmap: Bitmap, backupPixelX: Int, backupPixelY: Int): Boolean {
        // first try palette with a small color quant size
        val palette = Palette.from(bitmap).maximumColorCount(3).generate()
        return if (palette.swatches.size > 0) {
            isDark(palette) == IS_DARK
        } else {
            // if palette failed, then check the color of the specified pixel
            isDark(bitmap.getPixel(backupPixelX, backupPixelY))
        }
    }


    /**
     * Convert to HSL & check that the lightness value
     */
    fun isDark(@ColorInt color: Int): Boolean {
        val hsl = FloatArray(3)
        ColorUtils.colorToHSL(color, hsl)
        return isDark(hsl)
    }

    /**
     * Check that the lightness value (0–1)
     */
    fun isDark(hsl: FloatArray): Boolean { // @Size(3)
        return hsl[2] < 0.8f
    }


    fun getMostPopulousSwatch(palette: Palette?): Palette.Swatch? {
        var mostPopulous: Palette.Swatch? = null
        if (palette != null) {
            for (swatch in palette.swatches) {
                if (mostPopulous == null || swatch.population > mostPopulous.population) {
                    mostPopulous = swatch
                }
            }
        }
        return mostPopulous
    }

}