package com.ggx.easysp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ggx.sharepreference.AppConfig;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView t1;
    TextView t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1=findViewById(R.id.t1);
        t2=findViewById(R.id.t2);
        //获取对象实例
        final Random r=new Random();
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cat cat=AppConfig.create(getApplicationContext(),Cat.class);
                cat.setAge((float) r.nextInt(100));
                cat.setName("ggx cat"+r.nextInt(10));
                //保存Cat数据
                boolean result=AppConfig.save(cat);
                Toast.makeText(MainActivity.this,result?"保存成功"+cat.getAge():"保存失败"+cat.getAge(),Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cat cat=AppConfig.create(getApplicationContext(),Cat.class);
                t1.setText(cat.getName());
                t2.setText(String.valueOf(cat.getAge()));
            }
        });

    }
}
