package com.easy.framework.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.easy.framework.R;

public class CrashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);
        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        ((TextView) findViewById(R.id.tvMsg)).setText(message);
    }

    public void reStartApp(View view) {
        try {
            //todo com.easy.demo.ui.debug.DebugActivity 要换成对应的启动页面
            Class clz = Class.forName("com.easy.demo.ui.debug.DebugActivity");
            Intent intent = new Intent(this, clz);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
    }
}
