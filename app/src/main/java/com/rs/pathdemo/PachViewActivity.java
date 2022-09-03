package com.rs.pathdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rs.pathdemo.view.PathGLSurfaceView;

/**
 * @ProjectName: PathDemo
 * @Descpription:
 * @Author: liaorongsheng
 * @email: 1403233812@qq.com
 * @Date : 2022/9/1
 */
public class PachViewActivity extends AppCompatActivity {
    private static final String TAG = PachViewActivity.class.getSimpleName();
   private PathGLSurfaceView glsurfacecView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type = getIntent().getIntExtra("type", 1);
        Log.e(TAG, "onCreate  type  : " + type);
        int layout = 1;
        if (type == 1) {
            layout = R.layout.activity_type_one;
        } else if (type == 2) {
            layout = R.layout.activity_type_two;
        }

        setContentView(layout);

    }
}
