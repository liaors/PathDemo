package com.rs.pathdemo;

import android.graphics.Bitmap;
import android.view.Surface;

/**
 * @ProjectName: PathDemo
 * @Descpription:
 * @Author: liaorongsheng
 * @email: 1403233812@qq.com
 * @Date : 2022/9/3
 */
public class ShaderNative {

    static {
        System.loadLibrary("native-lib");
    }

    public static native void init(int width, int height, Surface surface, Bitmap defaultPaintTexture, ICallBack test);

    public static native void glDrawPaint(float[] drawPoint, int vertexCount, float textureRotate);

    public static native void glDrawData(float[] drawPoint,
                                         int vertexCount,
                                         Bitmap defaultPaintTexture,
                                         float width,
                                         int outType,
                                         boolean isTextureRotate,
                                         float A,
                                         float R,
                                         float G,
                                         float B, boolean isClear, boolean isDisplay);

    public static native void glPaintColor(float a, float r, float g, float b);

    public static native void onDestroy();

    public static native void glClearAll();

    public interface ICallBack {
       void voidCallBack();
    }

}
