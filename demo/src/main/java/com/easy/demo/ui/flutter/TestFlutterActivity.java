package com.easy.demo.ui.flutter;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;

import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.android.FlutterFragment;

@ActivityInject
@Route(path = "/demo/TestFlutterActivity", name = "flutter测试")
public class TestFlutterActivity extends AppCompatActivity {

    TextView tvScreen;
    EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_flutter);
        tvScreen = findViewById(R.id.tvScreen);
        editText = findViewById(R.id.editText);

        // 通过FlutterView引入Flutter编写的页面
//        FlutterView flutterView = new FlutterView(this);
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//        FrameLayout flContainer = findViewById(R.id.fl_container);
//        flContainer.addView(flutterView, lp);
//        flutterView.attachToFlutterEngine(flutterEngine);

        // 通过FlutterFragment引入Flutter编写的页面
//        FlutterFragment flutterFragment = FlutterFragment.createDefault();
//        FlutterFragment flutterFragment = FlutterFragment.withNewEngine()
//                .initialRoute("homePageRouter?{\"name\":\"" + getIntent().getStringExtra("name") + "\"}")
//                .build();
        FlutterFragment flutterFragment = FlutterFragment.withCachedEngine("my_engine_id").build();
//        MyFlutterFragment flutterFragment = MyFlutterFragment.newInstance("homePageRouter?{\"name\":\"" + getIntent().getStringExtra("name") + "\"}",
//                flutterEngine);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container, flutterFragment)
                .commit();

        FlutterChannel.setHandler((methodCall, result) -> {
            String data = methodCall.argument("message");
            tvScreen.setText("收到来自Flutter的数据：" + methodCall.method + "?msg=" + data);
        });
    }

    public void commit(View v) {
        String content = editText.getText().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("message", content);
        FlutterChannel.send(FlutterChannel.sendMethod, map);
    }
}
