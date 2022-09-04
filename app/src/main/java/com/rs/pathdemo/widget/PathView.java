package com.rs.pathdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: PathDemo
 * @Descpription:
 * @Author: liaorongsheng
 * @email: 1403233812@qq.com
 * @Date : 2022/9/3
 */
public class PathView extends View {
    private static final String TAG = PathView.class.getSimpleName();
    private Paint paint;
    private final int paintColor = Color.RED;
    private final int paintSize = 10;
    private List<BaseAction> mBaseActions;
    private BaseAction curAction;
    public PathView(Context context) {
        super(context);
        init();
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mBaseActions = new ArrayList<>();
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(20);
        paint.setColor(Color.RED);
        this.setFocusable(true);
    }


    float startX, startY;
   float x;
    float y;

 private   float touchX;
 private float touchY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_CANCEL) {
            return false;
        }

         touchX = event.getX();
         touchY = event.getY();
       switch (event.getAction()){
           case MotionEvent.ACTION_DOWN:
               setCurAction(touchX, touchY);
               Log.e(TAG, "ACTION_DOWN startX : "+startX + "  startY:"+startY );
               break;
           case MotionEvent.ACTION_MOVE:

                invalidate();
               Log.e(TAG, "onTouchEvent: ACTION_MOVE" );
               break;
           case MotionEvent.ACTION_CANCEL:
               Log.e(TAG, "onTouchEvent: ACTION_CANCEL" );
           case MotionEvent.ACTION_UP:
               Log.e(TAG, "onTouchEvent: UP" );
               mBaseActions.add(curAction);
               curAction = null;
               break;
           default:
               break;
       }

//        Log.e(TAG, "result: "+result );
        return true;
    }

    private void setCurAction(float x, float y) {
        curAction = new MyPath(x, y, paintSize, paintColor);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (BaseAction baseAction : mBaseActions) {
            baseAction.draw(canvas);
        }
        if(curAction != null){
            curAction.move(touchX, touchY);
            curAction.draw(canvas);
        }
    }
}
