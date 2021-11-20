package com.example.personnelmanagement;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class PersonnelReg extends AppCompatActivity {

    DBManager dbmanager;
    SQLiteDatabase sqlitedb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnel_reg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 액션 바에 메뉴 아이템 추가
        getMenuInflater().inflate(R.menu.menu_personnel_reg, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 클릭한 목록 아이템 ID
        int id = item.getItemId();

        // 클릭한 목록 아이템 ID별 이동할 액티비티
        if (id == R.id.action_settings) {
            Intent it = new Intent(this, MainActivity.class);
            startActivity(it);
            finish();
            return true;
        } else if (id == R.id.action_settings1) {
            Intent it = new Intent(this, PersonnelList.class);
            startActivity(it);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // '등록' 버튼 클릭 시
    public void register(View v) {
        // 1. 입력한 인물정보 추출
        // 성명
        EditText et_name = (EditText)findViewById(R.id.name);
        String str_name = et_name.getText().toString();

        // 성별
        RadioGroup rg_gender = (RadioGroup)findViewById(R.id.gender);
        RadioButton rb_gender;
        String str_gender = "";
        if (rg_gender.getCheckedRadioButtonId() == R.id.male) {
            rb_gender = (RadioButton)findViewById(R.id.male);
            str_gender = rb_gender.getText().toString();
        }
        if (rg_gender.getCheckedRadioButtonId() == R.id.female) {
            rb_gender = (RadioButton)findViewById(R.id.female);
            str_gender = rb_gender.getText().toString();
        }

        // 나이
        EditText et_age = (EditText)findViewById(R.id.age);
        String str_age = et_age.getText().toString();

        // 전화번호
        EditText et_tel = (EditText)findViewById(R.id.tel);
        String str_tel = et_tel.getText().toString();

        // 2. 테이블에 인물정보 추가
        try {
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getWritableDatabase();

            // 테이블에 추가할 데이터 할당
            ContentValues values = new ContentValues();
            values.put("name",   str_name);
            values.put("gender", str_gender);
            values.put("age", str_age);
            values.put("tel", str_tel);

            // 테이블에 추가
            long newRowId = sqlitedb.insert("Personnel", null, values);

            sqlitedb.close();
            dbmanager.close();

            // '인물정보' 액티비티로 이동
            Intent it = new Intent(this, PersonnelInfo.class);
            it.putExtra("it_name", str_name);
            startActivity(it);
            finish();
        } catch(SQLiteException e) {
            Toast.makeText(this,  e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}