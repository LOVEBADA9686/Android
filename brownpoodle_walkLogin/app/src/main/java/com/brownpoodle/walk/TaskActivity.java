package com.brownpoodle.walk;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class TaskActivity extends AsyncTask<String, Void, String> {

    String sendMsg, receiveMsg;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected String doInBackground(String... strings) { // 배열타입 task.execute(id, pw).get()값을
       System.out.println("TaskActivity.doInBackground >>> : 함수진입");

        try {
            String str;
            URL url = new URL("http://192.168.219.114:8088/kosmobp/walkDataSave.bp");
            System.out.println("url >>> : " + url);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            System.out.println("strings[] >>> : " + strings[0].toString());
            System.out.println("strings[] >>> : " + strings[1].toString());
            System.out.println("strings[] >>> : " + strings[2].toString());


            try {
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

                sendMsg = "m_id=" + "111" + "&longitude_str=" + strings[0] + "&latitude_str=" + strings[1] + "&walk_time=" + strings[2];
                System.out.println("sendMsg >>> : " + sendMsg);
                osw.write(sendMsg);
                osw.flush();
            } catch (Exception e) {
                System.out.println("e : " + e);
            }
            if(conn.getResponseCode() == conn.HTTP_OK){ // HTTP_OK = 200 = 성공
                System.out.println("conn.HTTP_OK >>> : 200");
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);
                BufferedReader br = new BufferedReader(tmp);
                StringBuffer sb = new StringBuffer();
                while((str= br.readLine()) != null){
                    sb.append(str);
                }
                receiveMsg = sb.toString();
                System.out.println(receiveMsg);
            }else{
                System.out.println("conn.HTTP_FAIL");
                System.out.println(conn.getResponseCode());
                InputStream err = conn.getErrorStream();
                System.out.println("err >>> : " + err);
            }
        } catch (Exception e){
            System.out.println("e" + e);
        }
        return receiveMsg;
    }

}
