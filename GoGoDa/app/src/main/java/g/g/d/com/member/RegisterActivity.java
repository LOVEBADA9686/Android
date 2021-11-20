package g.g.d.com.member;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import g.g.d.com.common.GogodaUtil;

public class RegisterActivity extends AsyncTask<String, Void, String> {

    String sendMsg, receiveMsg;

    protected String doInBackground(String... strings){
        System.out.println("RegisterActivity doInBackground >>>>>>>>>>>>>>>>>>>");
        try {
            String str;
            //  컨트롤러에 가서 jsp 넘어온 데이터를 받아야지
//            URL url = new URL("http://192.168.219.143:8080/gogodaProject/login/androidTest.jsp");
            String urlMapping = "";
            if(strings[0].equals("I")){
                urlMapping = "androidInsert.ggd";
                sendMsg = "mname=" + strings[1] + "&mid=" + strings[2] + "&mpw=" + strings[3] + "&mbirth=" + strings[4]
                        + "&mgender=" + strings[5] + "&mhp=" + strings[6] + "&memail=" + strings[7]
                        + "&mzipcode=" + strings[8] + "&maddr=" + strings[9] + "&maddrdetail=" + strings[10]
                        + "&positivefood=" + strings[11]  + "&negativefood=" + strings[12] + "&movietaste=" + strings[13];

            } else if (strings[0].equals("IDCHECK")) {
                urlMapping = "idCheck.ggd";
                sendMsg = "mid=" + strings[1];
            }
            String urlstr = "http://"+ GogodaUtil.URL_PATH +"/springProject/mem/";

            URL url = new URL(urlstr + urlMapping);
            System.out.println("url >>> : " + url);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

            osw.write(sendMsg);
            osw.flush();

            if (conn.getResponseCode() == conn.HTTP_OK){
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();

                while ((str=reader.readLine()) !=null){
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
            }else{
            }
        }catch (Exception e){
        }
        return receiveMsg;
    }
}
