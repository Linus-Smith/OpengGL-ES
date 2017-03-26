package com.chyang.chapter_6;

import android.content.Intent;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.chyang.chapter_6.activity.Example6_3;
import com.chyang.chapter_6.activity.Example6_3_1_dome;
import com.chyang.chapter_6.activity.Example6_6;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_e6_3).setOnClickListener(this);
        findViewById(R.id.bt_e6_6).setOnClickListener(this);
        findViewById(R.id.bt_6_3_dome).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent mIntent = new Intent();
        switch (v.getId()) {
            case R.id.bt_e6_3:
                mIntent.setClass(this, Example6_3.class);
                break;
            case R.id.bt_e6_6:
                mIntent.setClass(this, Example6_6.class);
                break;
            case R.id.bt_6_3_dome:
                mIntent.setClass(this, Example6_3_1_dome.class);
                break;
        }
        startActivity(mIntent);
    }
}
