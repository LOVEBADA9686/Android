package com.example.personnelmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PersonnelList extends AppCompatActivity implements OnClickListener {

    DBManager dbmanager;
    SQLiteDatabase sqlitedb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnel_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 인물정보 레이아웃 인식
        LinearLayout layout = (LinearLayout)findViewById(R.id.personnel);

        try {
            // Personnel 테이블에서 인물정보 추출
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getReadableDatabase();
            Cursor cursor = sqlitedb.query("Personnel", null, null, null, null, null, null);

            // 각 인물정보의 반복 출력을 통한 목록화
            int i = 0;
            while(cursor.moveToNext()) {
                // 1. 인물 정보 추출
                String str_name   = cursor.getString(cursor.getColumnIndex("name"));
                String str_gender = cursor.getString(cursor.getColumnIndex("gender"));
                int age           = cursor.getInt(cursor.getColumnIndex("age"));
                String str_tel    = cursor.getString(cursor.getColumnIndex("tel"));

                // 2. 인물정보 목록 아이템 만들기
                LinearLayout layout_item = new LinearLayout(this);
                layout_item.setOrientation(LinearLayout.VERTICAL);
                layout_item.setPadding(20,  10,  20,  10);
                layout_item.setId(i);
                layout_item.setTag(str_name);

                // 3.1 성명
                TextView tv_name = new TextView(this);
                tv_name.setText(str_name);
                tv_name.setTextSize(30);
                tv_name.setBackgroundColor(Color.argb(50, 0, 255, 0));
                // tv_name.setPadding(5, 5, 5, 5);
                layout_item.addView(tv_name);

                // 3.2 성별
                TextView tv_gender = new TextView(this);
                tv_gender.setText("성별: " + str_gender);
                layout_item.addView(tv_gender);

                // 3.3 나이
                TextView tv_age = new TextView(this);
                tv_age.setText("나이: " + age);
                layout_item.addView(tv_age);

                // 3.4 전화번호
                TextView tv_tel = new TextView(this);
                tv_tel.setText("전화번호: " + str_tel);
                layout_item.addView(tv_tel);

                // 3.5 클릭 리스너 설정
                layout_item.setOnClickListener(this);

                // 3.6 인물정보 레이아웃에 인물정보 추가
                layout.addView(layout_item);

                i++;
            }

            cursor.close();
            sqlitedb.close();
            dbmanager.close();

        } catch(SQLiteException e) {
            Toast.makeText(this,  e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        // 클릭한 목록 아이템 인식
        LinearLayout layout_item = (LinearLayout)findViewById(id);
        String str_name = (String)layout_item.getTag();

        // 인물정보 액티비티 호출
        Intent it = new Intent(this, PersonnelInfo.class);
        it.putExtra("it_name", str_name);
        startActivity(it);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 액션 바에 메뉴 아이템 추가
        getMenuInflater().inflate(R.menu.menu_personnel_list, menu);
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
        } else if (id == R.id.action_settings2) {
            Intent it = new Intent(this, PersonnelReg.class);
            startActivity(it);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
