#include <jni.h>
#include <logUtil.h>
#include "WhiteboardEngine.h"
#include "shader/SquareShader.h"
#include "shader/TextureImageDemo.h"
#include <android/bitmap.h>
#include <malloc.h>
#include <string.h>


WhiteboardEngine *whiteboardEngine = nullptr;


ImageInfo *bitmapToImageInfo(JNIEnv *env, jobject &bitmap) {
    AndroidBitmapInfo info; // create a AndroidBitmapInfo
    int result;
    // 获取图片信息
    result = AndroidBitmap_getInfo(env, bitmap, &info);
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGCATE("AndroidBitmap_getInfo failed, result: %d", result);
        return nullptr;
    }
    // 获取像素信息
    unsigned char *data;
    result = AndroidBitmap_lockPixels(env, bitmap, reinterpret_cast<void **>(&data));
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGCATE("AndroidBitmap_lockPixels failed, result: %d", result);
        return nullptr;
    }
    size_t count = info.stride * info.height;

    unsigned char *resultData = (unsigned char *) malloc(count * sizeof(unsigned char));
    memcpy(resultData, data, count);

    // 像素信息不再使用后需要解除锁定
    result = AndroidBitmap_unlockPixels(env, bitmap);
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGCATE("AndroidBitmap_unlockPixels failed, result: %d", result);
    }

    ImageInfo *brushPoints = new ImageInfo(info.width, info.height, resultData);
    return brushPoints;
}


extern "C" JNIEXPORT void JNICALL
Java_com_rs_pathdemo_ShaderNative_init(JNIEnv *env, jclass clazz, jint width,
                                          jint height, jobject surface, jobject bitmap,
                                          jobject jCallback) {
    if (whiteboardEngine != nullptr) {
        delete whiteboardEngine;
        whiteboardEngine = nullptr;
    }

    //初始化EGL
    whiteboardEngine = new WhiteboardEngine(surface, env);
    whiteboardEngine->init(width, height, bitmapToImageInfo(env, bitmap), env, jCallback);
}

extern "C" JNIEXPORT void JNICALL
Java_com_rs_pathdemo_ShaderNative_glDrawPaint(JNIEnv *env, jclass instance, jfloatArray point,
                                                 jint verTextSize, jfloat calulateXYAnagle) {

    if (whiteboardEngine == nullptr) return;

    jfloat *pDouble = env->GetFloatArrayElements(point, nullptr);
    whiteboardEngine->glDrawPoints(pDouble, verTextSize, calulateXYAnagle);
}

extern "C" JNIEXPORT void JNICALL
Java_com_rs_pathdemo_ShaderNative_glDrawData(JNIEnv *env, jclass instance, jfloatArray point,
                                                jint verTextSize,
                                                jobject bitmap, jfloat brushWidth,
                                                jint outType,
                                                jboolean isTextureRotate,
                                                jfloat A,
                                                jfloat R,
                                                jfloat G,
                                                jfloat B,
                                                jboolean isClear,
                                                jboolean isDisplay) {
    if (whiteboardEngine == nullptr) return;

    BrushInfo::OutType setOutType;
    switch (outType) {
        case 1:
            setOutType = BrushInfo::OutType::ERASER;
            break;
        default:
            setOutType = BrushInfo::OutType::DRAW;
            break;
    }

    jfloat *pDouble = point == nullptr ? nullptr : env->GetFloatArrayElements(point, nullptr);

    //不等于空说明需要重新设置纹理图片
    if (bitmap != nullptr) {
        ImageInfo *pInfo = bitmapToImageInfo(env, bitmap);
        whiteboardEngine->glDrawData(pDouble, verTextSize, pInfo, brushWidth, setOutType,
                                     isTextureRotate, isClear,
                                     isDisplay, A, R, G, B);
    } else {
        whiteboardEngine->glDrawData(pDouble, verTextSize, nullptr, brushWidth, setOutType,
                                     isTextureRotate, isClear,
                                     isDisplay, A, R, G, B);
    }
}



extern "C" JNIEXPORT void JNICALL
Java_com_rs_pathdemo_ShaderNative_glPaintColor(JNIEnv *env, jclass instance, jfloat a, jfloat r,
                                                  jfloat g, jfloat b) {
    if (whiteboardEngine == nullptr) return;

    whiteboardEngine->setPaintColor(a, r, g, b);

}


extern "C" JNIEXPORT void JNICALL
Java_com_rs_pathdemo_ShaderNative_onDestroy(JNIEnv *env, jclass instance) {
    if (whiteboardEngine != nullptr) {
        delete whiteboardEngine;
        whiteboardEngine = nullptr;
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_rs_pathdemo_ShaderNative_glClearAll(JNIEnv *env, jclass instance) {
    if (whiteboardEngine != nullptr) {
        whiteboardEngine->glClearColor();
    }
}



