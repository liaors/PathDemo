package com.rs.pathdemo.util.pen.page

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import com.rs.pathdemo.R
import com.rs.pathdemo.util.pen.TexturePen

/**
 * @ProjectName: PathDemo
 * @Descpription:
 * @Author: liaorongsheng
 * @email: 1403233812@qq.com
 * @Date : 2022/9/3
 */
data class BrushRes(val id: String, val resBrush: Int, var isRotate: Boolean = true)

data class BrushInfoConfig(
    var res: BrushRes?,
    var brushWidth: Float,
    var outType: Int,
    var points: FloatArray? = null,
    var vertexCount: Int = 0,
    var currColorInt: Int = Color.RED
)

object BrushManager {

    fun getDefaultBrushInfoConfig(): BrushInfoConfig {
        return BrushInfoConfig(getDefaultBrushRes(), 15f, TexturePen.DRAW)
    }

    /**
     * 是否是同一个id
     */
    fun isAgainSetTexture(id: String?, info: BrushInfoConfig): BrushRes? {
        return if (id == info.res?.id) {
            null
        } else {
            info.res
        }
    }

    private fun getDefaultBrushRes(): BrushRes {
        return BrushRes("1", R.drawable.brush_paint_normal_128)
    }

    fun getBrushList(): MutableList<BrushRes> {
        return mutableListOf(
            getDefaultBrushRes(),
            BrushRes("2", R.drawable.brush_paint_crayon2_128),
            BrushRes("3", R.drawable.brush_paint_marker_128, false),
            BrushRes("4", R.drawable.brush_paint_blur_128),
            BrushRes("5", R.drawable.brush_paint_crayon_128)
        )
    }

    fun getBrushBitmap(resources: Resources, brushRes: BrushRes?): Bitmap? {
        brushRes?.apply {
            return BitmapFactory.decodeResource(resources, brushRes.resBrush)
        }
        return null
    }


}
