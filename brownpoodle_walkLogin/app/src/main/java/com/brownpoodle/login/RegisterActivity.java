package com.brownpoodle.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.brownpoodle.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AsyncTask<String, Void, String> {

    String sendMsg, receiveMsg;

    @Override
    protected String doInBackground(String... strings) { // 배열타입 task.execute(id, pw).get()값을
       System.out.println("RegisterActivity.doInBackground >>> : 함수진입");

        try {
            String str;
            URL url = new URL("http://192.168.219.114:8088/kosmobp/loginTry.bp");
            System.out.println("url >>> : " + url);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            System.out.println("strings[] >>> : " + strings[0]);
            System.out.println("strings[] >>> : " + strings[1]);

            try {
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                //id, pw
                sendMsg = "m_id=" + strings[0] + "&m_pw=" + strings[1];
                System.out.println("sendMsg >>> : " + sendMsg);
                osw.write(sendMsg);
                osw.flush();
            } catch (Exception e) {
                System.out.println("e : " + e);
            }

            if(conn.getResponseCode() == conn.HTTP_OK){ // HTTP_OK = 200 = 성공
                System.out.println("conn.HTTP_OK >>> : 200");
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(tmp);
                StringBuffer sb = new StringBuffer();
                while((str= br.readLine()) != null){
                    sb.append(str);
                }
                receiveMsg = sb.toString();
                System.out.println(receiveMsg);

            }else{
                System.out.println("conn.HTTP_FAIL >>> : 400");
            }
        } catch (Exception e){ }
        return receiveMsg;
    }
}
