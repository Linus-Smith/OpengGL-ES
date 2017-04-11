package com.linus.opengl_es_two;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.linus.opengl_es_two.Sample3.Activity.Sample3_1Activity;
import com.linus.opengl_es_two.Sample5.c_5_1.Sample5_1_Activity;
import com.linus.opengl_es_two.Sample5.c_5_2.Sample5_2_Activity;
import com.linus.opengl_es_two.Sample5.c_5_3.Sample5_3_Activity;
import com.linus.opengl_es_two.Sample5.c_5_4.Sample5_4_Activity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_s3_1).setOnClickListener(this);
        findViewById(R.id.bt5_1).setOnClickListener(this);
        findViewById(R.id.bt_5_2).setOnClickListener(this);
        findViewById(R.id.bt_5_3).setOnClickListener(this);
        findViewById(R.id.bt_5_4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent mIntent = new Intent();
        switch (id) {
            case R.id.bt_s3_1:
                mIntent.setClass(this, Sample3_1Activity.class);
                break;
            case R.id.bt5_1:
                mIntent.setClass(this, Sample5_1_Activity.class);
                break;
            case R.id.bt_5_2:
                mIntent.setClass(this, Sample5_2_Activity.class);
                break;
            case R.id.bt_5_3:
                mIntent.setClass(this, Sample5_3_Activity.class);
                break;
            case R.id.bt_5_4:
                mIntent.setClass(this, Sample5_4_Activity.class);
                break;
        }
        startActivity(mIntent);
    }
}
