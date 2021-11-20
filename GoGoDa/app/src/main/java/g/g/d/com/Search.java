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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import g.g.d.com.R;
import g.g.d.com.common.GogodaUtil;

public class Search extends AppCompatActivity {

    EditText mName ;
    EditText mEmail ;
    private String mJsonString;
    private static String IP_ADDRESS = GogodaUtil.URL_PATH;
    private static String TAG = "찾기 :";
    private String sType = "";
    private String sMid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mName = (EditText) findViewById(R.id.u_name);
        mEmail = (EditText) findViewById(R.id.u_mail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            LOGDT data = extras.getParcelable("data");
            if (data != null) {
                sType = data.sType;
                if (sType.equals("PW")){
                    mName.setHint((CharSequence) "ID");
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Search.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("Search", "response - " + result);

            if (result == null){


            }
            else {
                if (sType.equals("ID")){
                    if (result.equals("ID_fail")){

                        Toast.makeText(getApplicationContext(), "아이디가 존재하지 않습니다.!" , Toast.LENGTH_SHORT).show();

                    }else{
                        mJsonString = result;
                        showResult();
                        Intent intent = new Intent(Search.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    if (result.equals("PW_fail")){
                        Toast.makeText(getApplicationContext(), "비밀번호가 맞지 않습니다.!" , Toast.LENGTH_SHORT).show();

                    }else{
                        String sMail = result;
                        sMid = mName.getText().toString();
                        sendMail("PW", sMail);
                        Intent intent = new Intent(Search.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }
                progressDialog.dismiss();

            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String key = (String) params[1];
            String value = (String) params[2];
            String key2 = (String) params[3];
            String value2 = (String) params[4];
            String postParameters = key + "=" + value + "&" + key2 + "=" + value2;
            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();

            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();
                return null;
            }
        }
    }


    private void showResult(){

        String TAG_JSON="result";
        String TAG_MID = "mid";
        String TAG_EMAIL = "email";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);
                String mEmail = item.getString(TAG_EMAIL);
                sMid = item.getString(TAG_MID);
                if (mEmail.length()!=0){
                    sendMail("ID", mEmail);

                }
            }


        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    private void sendMail(String schMd , String sMail){

        String message = "상기메일은 고고닥 닷컴에서 " + sMid + " 님의 " ;
        if (sType.equals("ID")){
            message = message + "ID를 발송하기 위한 메일입니다." + "\n" +"아이디 : " + sMid ;
        }else{
            String password = randomPassword(10);
            message = message + "임시 비밀번호를 발송하기 위한 메일입니다." + "\n" +"임시 비밀번호: " + password ;
        }
        try {
            GMailSender gMailSender = new GMailSender("tadeos1227@gmail.com", "ohho1234");
            //GMailSender.sendMail(제목, 본문내용, 받는사람);
            gMailSender.sendMail("제목입니다", message , sMail);
            Toast.makeText(getApplicationContext(), "이메일을 발송 하였습니다.", Toast.LENGTH_SHORT).show();
        } catch (SendFailedException e) {
            Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sch_btn(View target) {
        GetData task = new GetData();
        if (sType.equals("ID")){
            task.execute( "http://" + IP_ADDRESS + "/getID.jsp","mname", mName.getText().toString(),"memail",mEmail.getText().toString());
        }else{

            task.execute( "http://" + IP_ADDRESS + "/getEmail.jsp","mid", mName.getText().toString(),"memail",mEmail.getText().toString());

        }
    }

    public static String randomPassword (int length) {
        int index = 0;
        char[] charSet = new char[] {
                '0','1','2','3','4','5','6','7','8','9'
                ,'A','B','C','D','E','F','G','H','I','J','K','L','M'
                ,'N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
                ,'a','b','c','d','e','f','g','h','i','j','k','l','m'
                ,'n','o','p','q','r','s','t','u','v','w','x','y','z'};

        StringBuffer sb = new StringBuffer();
        for (int i=0; i<length; i++) {
            index =  (int) (charSet.length * Math.random());
            sb.append(charSet[index]);
        }

        return sb.toString();

    }


}