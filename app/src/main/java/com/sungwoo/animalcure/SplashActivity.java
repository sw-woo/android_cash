package com.sungwoo.animalcure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,ViewPagerActivity.class);
                startActivity(intent);
                finish(); // 현재 액티비티를 삭제.
            }
        }, 3000); // 3초 정도 딜레이를 준 후 시작
    }
}