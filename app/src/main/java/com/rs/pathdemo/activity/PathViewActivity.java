package com.rs.pathdemo.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rs.pathdemo.R;


/**
 * @ProjectName: PathDemo
 * @Descpription:
 * @Author: liaorongsheng
 * @email: 1403233812@qq.com
 * @Date : 2022/9/3
 */
public class PathViewActivity extends AppCompatActivity {
    private static final String TAG = PathViewActivity.class.getSimpleName();
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
        findViewById(R.id.back_bt).setOnClickListener(v -> finish());
    }
}
