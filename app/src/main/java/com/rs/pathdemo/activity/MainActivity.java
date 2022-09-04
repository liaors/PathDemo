package com.rs.pathdemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rs.pathdemo.R;

public class MainActivity extends AppCompatActivity {
private static final String TYPE = "type";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, PathViewActivity.class);
        switch (view.getId()){
            case R.id.type_one_bt:
                intent.putExtra(TYPE, 1);
                startActivity(intent);
                break;
            case R.id.type_two_bt:
                intent.putExtra(TYPE, 2);
                startActivity(intent);
                break;
            case R.id.type_three_bt:
                intent = new Intent(this, GLActivity.class);
                intent.putExtra(TYPE, 3);
                startActivity(intent);
                break;
        }
    }
}