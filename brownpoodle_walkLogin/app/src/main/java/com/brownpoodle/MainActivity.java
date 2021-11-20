package com.brownpoodle;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.brownpoodle.login.MainActivity_login;
import com.brownpoodle.walk.MainActivity_walk;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("onCreate >>> : ");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("갈색푸들"); //타이틀 없음

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings1:
                Intent it;
                it = new Intent(getApplicationContext(), MainActivity_login.class);
                startActivity(it);
                finish();
                return true;
        }

        switch (item.getItemId()) {
            case R.id.action_settings2:
                Intent it;
                it = new Intent(getApplicationContext(), MainActivity_walk.class);
                startActivity(it);
                finish();
                return true;
        }

        switch (item.getItemId()) {
            case R.id.action_settings3:
                Intent it;
                it = new Intent(getApplicationContext(), MainActivity_walk.class);
                startActivity(it);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

