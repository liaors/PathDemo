package com.rs.pathdemo

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.rs.pathdemo.pen.page.BrushManager
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
        val whiteView = findViewById<WhiteboardTextureView>(R.id.white_view)
        val touch_view = findViewById<TouchView>(R.id.touch_view)
        whiteView.init(touch_view, object : WhiteboardTextureView.IWhiteboardStatus {
            override fun hasPen(undoDisable: Boolean, redoDisable: Boolean) {
                initColor()
            }
        }, currBrushConfig)
        Handler().postDelayed({
            initColor()
        }, 200)
        findViewById<Button>(R.id.back_bt).setOnClickListener {
            finish()
        }
    }

    private fun initColor() {
        val it = Color.RED
        val a = Color.alpha(it) * 1f / 255f
        val r = Color.red(it) * 1f / 255f
        val g = Color.green(it) * 1f / 255f
        val b = Color.blue(it) * 1f / 255f
        ShaderNative.glPaintColor(a, r, g, b)
    }


}
