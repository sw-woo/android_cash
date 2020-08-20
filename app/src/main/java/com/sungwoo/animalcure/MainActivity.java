package com.sungwoo.animalcure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView tv_title; //텍스트 변수 선언
    TextView tv_title2;
    Button btn_research;// 버튼 뷰 변수를 선언,
    EditText et_name; // 에딧 텍스트 뷰 변수를 선언.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_title2 = findViewById(R.id.textview2);
        btn_research =findViewById(R.id.btn_research);
        et_name = findViewById(R.id.et_name);


        tv_title2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_title2.setText("wellcome");
            }
        });

        btn_research.setOnClickListener(new View.OnClickListener() { //해당 뷰를 클릭할 경우 수행
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, et_name.getText().toString()+"근처 동물 병원을 찾고 있습니다.",Toast.LENGTH_LONG).show();
            }
        });
    }
}