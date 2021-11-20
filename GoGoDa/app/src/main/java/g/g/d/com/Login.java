package g.g.d.com;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import g.g.d.com.R;
import g.g.d.com.common.GogodaUtil;
import g.g.d.com.member.MemberActivity;


public class Login extends AppCompatActivity {
    EditText edit_id ;
    EditText edit_pw ;
    private static final String USER_AGENT = "Mozilla/5.0";
    private String mJsonString;
    private static String IP_ADDRESS = GogodaUtil.URL_PATH + "";
    //private static String IP_ADDRESS = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edit_id = (EditText) findViewById(R.id.login_id);
        edit_pw = (EditText) findViewById(R.id.login_pw);
    }


    public void join_btn(View view) {
        // 회원가입 버튼 MainActivity 를 회원가입 클래스로 교체
        Intent intent = new Intent( Login.this, MemberActivity.class);
        startActivity(intent);
//        finish();
       }

    public void login_btn(View view) {
        GetLogin task = new GetLogin();
//        task.execute( "http://" + IP_ADDRESS + "/getLogin.jsp","");
        task.execute( "http://" + IP_ADDRESS + "/springProject/logincontroller/loginresult.ggd","");
    }

    @SuppressLint("StaticFieldLeak")
    public class GetLogin extends AsyncTask<String, Void, String> {

        private static final String TAG = "GetLogin";
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Login.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "response - " + result);

            if (result == null){
                Toast.makeText(getApplicationContext(), errorString , Toast.LENGTH_SHORT).show();
            }
            else {
//                if (result.equals("Success")){
                if (result.equals("login_ok")){
                    SaveSharedPreference.setUserName(Login.this, edit_id.getText().toString());
                    Toast.makeText(getApplicationContext(), "로그인 성공.!!" , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    if (result.equals("discord_pw")){
                        Toast.makeText(getApplicationContext(), "아이디가 존재하지 않습니다.!" , Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getApplicationContext(), "비밀번호가 맞지 않습니다.!" , Toast.LENGTH_SHORT).show();
                    }
                }
                progressDialog.dismiss();
                Log.d(TAG, "POST response  - " + result);
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = (String) params[0] + "?mid=" + edit_id.getText().toString()
                                    + "&mpw=" + edit_pw.getText().toString();

            try {

                URL url = new URL(serverURL);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("User-Agent", USER_AGENT);
                httpURLConnection.setReadTimeout(5000);

                httpURLConnection.setConnectTimeout(5000);

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();

                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "GET response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {

                      inputStream = httpURLConnection.getInputStream();
                } else {

                        inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();
                return sb.toString();

            } catch (Exception e) {

                Log.d(TAG, "Data: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
    public void id_sch(View view) {

        LOGDT data = new LOGDT("","ID","" ) ;
        Intent intent = new Intent(getApplicationContext(), Search.class);
        intent.putExtra("data", data);
        startActivity(intent);
    }

    public void pw_sch(View view) {

        LOGDT data = new LOGDT("","PW","" ) ;
        Intent intent = new Intent(getApplicationContext(), Search.class);
        intent.putExtra("data", data);
        startActivity(intent);
    }

}




