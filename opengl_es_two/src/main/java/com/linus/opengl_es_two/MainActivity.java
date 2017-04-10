package com.linus.opengl_es_two;

import android.content.Intent;
import android.opengl.GLES30;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.linus.opengl_es_two.Sample3.Activity.Sample3_1Activity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_s3_1).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent mIntent = new Intent();
        switch (id) {
            case R.id.bt_s3_1:
                mIntent.setClass(this, Sample3_1Activity.class);
                break;
        }
        startActivity(mIntent);
    }
}
