package com.brownpoodle.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.brownpoodle.MainActivity;
import com.brownpoodle.R;

public class MainActivity_login extends AppCompatActivity {

    Toolbar toolbar;

    Button regibtn;
    EditText mid, mpw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        System.out.println("onCreate >>> : ");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("갈색푸들"); //타이틀 없음

        setTitle("로그인");

        regibtn = (Button)findViewById(R.id.register_btn);
        mid = (EditText)findViewById(R.id.register_id);
        mpw = (EditText)findViewById(R.id.register_pw);

        regibtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                try{
                    Toast.makeText(MainActivity_login.this,
                            "로그인",
                            Toast.LENGTH_SHORT).show();
                    String result;
                    String m_id = mid.getText().toString();
                    String m_pw = mpw.getText().toString();
                    System.out.println("id >>> : " + m_id);
                    System.out.println("pw >>> : " + m_pw);

                    RegisterActivity task = new RegisterActivity();
                    result = task.execute(m_id, m_pw).get();
                    System.out.println("result >>> : " + result);

                    // 로그인 후 메인으로 이동
                    Intent it = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(it);
                    finish();

                } catch (Exception e) {
                    Log.i("DBTEST", "ERROR");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

}