package com.rs.pathdemo.util.pen

import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import com.rs.pathdemo.util.pen.page.BrushInfoConfig

/**
 * @ProjectName: PathDemo
 * @Descpription:
 * @Author: liaorongsheng
 * @email: 1403233812@qq.com
 * @Date : 2022/9/3
 */
interface IPen {
    fun downEvent(pagerView: View, coordinate:PointF,event: MotionEvent)
    fun moveEvent(pagerView: View, coordinate:PointF,event: MotionEvent)
    fun upEvent(pagerView: View, coordinate:PointF,event: MotionEvent)
    fun getDrawPoints(): ArrayList<Float>
    fun drawPoints(points: FloatArray, textureRotate: Float)

    fun rest()
    fun copy(brushInfoConfig: BrushInfoConfig): IPen
    fun getConfig(): BrushInfoConfig?
}