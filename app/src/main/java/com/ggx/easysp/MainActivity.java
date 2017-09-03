package com.ggx.easysp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ggx.sharepreference.AppConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取对象实例
        final Cat cat=AppConfig.create(getApplicationContext(),Cat.class);
        cat.getName();
        System.out.println(cat.getAge());

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat.setAge(50);
                //保存Cat数据
                boolean result=AppConfig.save(Cat.class);
                Toast.makeText(MainActivity.this,result?"保存成功":"保存失败",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
