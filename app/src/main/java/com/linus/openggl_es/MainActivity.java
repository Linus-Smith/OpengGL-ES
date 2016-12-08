package com.linus.openggl_es;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.linus.openggl_es.activity.HelloOpenGLAc;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_hello_gl).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent mIntent = new Intent();
        switch (view.getId()) {
            case R.id.bt_hello_gl:
                mIntent.setClass(this, HelloOpenGLAc.class);
                break;
        }
        startActivity(mIntent);
    }
}
