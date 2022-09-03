package com.rs.pathdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: PathDemo
 * @Descpription:
 * @Author: liaorongsheng
 * @email: 1403233812@qq.com
 * @Date : 2022/9/3
 */
public class PathSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mSurfaceHolder = null;

    private BaseAction curAction = null;
    private int paintColor = Color.RED;
    private int paintSize = 10;

    private Paint mPaint;

    private List<BaseAction> mBaseActions;

    private Bitmap mBitmap;

    private ActionType mActionType = ActionType.Path;

    public PathSurfaceView(Context context) {
        super(context);
        init();
    }

    public PathSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public PathSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        this.setFocusable(true);

        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(paintSize);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = mSurfaceHolder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        mSurfaceHolder.unlockCanvasAndPost(canvas);
        mBaseActions = new ArrayList<>();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_CANCEL) {
            return false;
        }

        float touchX = event.getX();
        float touchY = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setCurAction(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                Canvas canvas = mSurfaceHolder.lockCanvas();
                canvas.drawColor(Color.WHITE);
                for (BaseAction baseAction : mBaseActions) {
                    baseAction.draw(canvas);
                }
                curAction.move(touchX, touchY);
                curAction.draw(canvas);
                mSurfaceHolder.unlockCanvasAndPost(canvas);
                break;
            case MotionEvent.ACTION_UP:
                mBaseActions.add(curAction);
                curAction = null;
                break;

            default:
                break;
        }
        return true;
    }

    private void setCurAction(float x, float y) {
        curAction = new MyPath(x, y, paintSize, paintColor);
    }


    public Bitmap getBitmap() {
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBitmap);
        doDraw(canvas);
        return mBitmap;
    }

    public String saveBitmap(PathSurfaceView pathSurfaceView) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/pathview/" + System.currentTimeMillis() + ".png";
        if (!new File(path).exists()) {
            new File(path).getParentFile().mkdir();
        }
        savePicByPNG(pathSurfaceView.getBitmap(), path);
        return path;
    }

    public static void savePicByPNG(Bitmap bitmap, String filePath) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(filePath);
            if (null != fileOutputStream) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        for (BaseAction action : mBaseActions) {
            action.draw(canvas);
        }
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
    }


    /**
     * 回退
     *
     * @return 是否已经回退成功
     */
    public boolean back() {
        if (mBaseActions != null && mBaseActions.size() > 0) {
            mBaseActions.remove(mBaseActions.size() - 1);
            Canvas canvas = mSurfaceHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            for (BaseAction action : mBaseActions) {
                action.draw(canvas);
            }
            mSurfaceHolder.unlockCanvasAndPost(canvas);
            return true;
        }
        return false;
    }

    public void reset() {
        if (mBaseActions != null && mBaseActions.size() > 0) {
            mBaseActions.clear();
            Canvas canvas = mSurfaceHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            for (BaseAction action : mBaseActions) {
                action.draw(canvas);
            }
            mSurfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    enum ActionType {
        Point, Path, Line, Rect, Circle, FillEcRect, FilledCircle
    }
}