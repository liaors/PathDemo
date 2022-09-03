package com.rs.pathdemo.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.rs.pathdemo.PathRender;

/**
 * @ProjectName: PathDemo
 * @Descpription:
 * @Author: liaorongsheng
 * @email: 1403233812@qq.com
 * @Date : 2022/9/3
 */
public class PathGLSurfaceView extends GLSurfaceView {
private PathRender pathRender;

    public PathGLSurfaceView(Context context) {
        super(context);
        init();
    }

    public PathGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        pathRender = new PathRender();
       setEGLContextClientVersion(2);
       setEGLConfigChooser(false);
       setRenderer(pathRender);
       setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pathRender.POINT_DATA[0] = touchX;
                pathRender.POINT_DATA[1] = touchY;
                requestRenderPath();
                break;
            case MotionEvent.ACTION_MOVE:
                pathRender.POINT_DATA[2] = touchX;
                pathRender.POINT_DATA[3] = touchY;
                requestRenderPath();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void  requestRenderPath(){
       requestRender();
   }
}
