package com.rs.pathdemo.pen.page

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.rs.pathdemo.R
import com.rs.pathdemo.ShaderNative
import com.rs.pathdemo.widget.TouchView
import com.rs.pathdemo.widget.WhiteboardTextureView


/**
 * @ProjectName: PathDemo
 * @Descpription:
 * @Author: liaorongsheng
 * @email: 1403233812@qq.com
 * @Date : 2022/9/3
 */
class GLActivity : AppCompatActivity() {
    //默认配置
    private var currBrushConfig = BrushManager.getDefaultBrushInfoConfig()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_three)
        val whiteView = findViewById(R.id.white_view) as WhiteboardTextureView
        val touch_view = findViewById(R.id.touch_view) as TouchView
        whiteView.init(touch_view, object : WhiteboardTextureView.IWhiteboardStatus {
            override fun hasPen(undoDisable: Boolean, redoDisable: Boolean) {
                val it = Color.RED
                val a = Color.alpha(it) * 1f / 255f
                val r = Color.red(it) * 1f / 255f
                val g = Color.green(it) * 1f / 255f
                val b = Color.blue(it) * 1f / 255f
                ShaderNative.glPaintColor(a, r, g, b)
            }
        }, currBrushConfig)

        Handler().postDelayed(Runnable {
            val it = Color.RED
            val a = Color.alpha(it) * 1f / 255f
            val r = Color.red(it) * 1f / 255f
            val g = Color.green(it) * 1f / 255f
            val b = Color.blue(it) * 1f / 255f
            ShaderNative.glPaintColor(a, r, g, b)
        }, 200)


    }


}
