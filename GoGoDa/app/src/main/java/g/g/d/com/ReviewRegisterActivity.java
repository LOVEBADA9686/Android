package g.g.d.com;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import g.g.d.com.common.GogodaUtil;

public class ReviewRegisterActivity extends AsyncTask<String, Void, String> {

    String sendMsg, receiveMsg;

    @Override
    protected String doInBackground(String... strings) {
        try{
            String str = "";
            String urlmapping = "";
            String urlStr = "http://"+ GogodaUtil.URL_PATH +"/springProject/review/";
            if(strings[0].toUpperCase().equals("I")){
                urlmapping = "reviewAndroidInsert.ggd";
                sendMsg = "kakaoid=" + strings[1] + "&renickname=" + strings[2] + "&recontent=" + strings[3] + "&rerating=" + strings[4];
            }else if(strings[0].toUpperCase().equals("U")){
                urlmapping = "reviewAndroidUpdate.ggd";
                sendMsg = "kakaoid=" + strings[1] + "&renum=" + strings[2] + "&recontent=" + strings[3] + "&rerating=" + strings[4];
            }else if(strings[0].toUpperCase().equals("D")){
                urlmapping = "reviewAndroidDelete.ggd";
                sendMsg = "kakaoid=" + strings[1] + "&renum=" + strings[2];
            }else if(strings[0].toUpperCase().equals("RA")) {
                urlmapping = "reviewRating.ggd";
                sendMsg = "kakaoid=" + strings[1];
            }else if(strings[0].toUpperCase().equals("EMO")){
                urlStr = "http://" + GogodaUtil.URL_PATH + "/springProject/emotion/androidEmotionSearch.ggd";
                sendMsg = "text=" + strings[1];
            }else if(strings[0].toUpperCase().equals("ADDR")){
                urlmapping = "memAddrSelect.ggd";
                sendMsg = "mid=" + strings[1];
            }
//            else if(strings[0].toUpperCase().equals("ADDR")){
//                urlmapping = "";
//            }
            urlStr += urlmapping;


            URL url = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");

            //System.out.println(URLDecoder.decode(sendMsg, "utf-8"));
            osw.write(sendMsg);
            osw.flush();

            if(conn.getResponseCode() == conn.HTTP_OK){
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(),"UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();

                while((str = reader.readLine()) != null){
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
            }else{
                System.out.println("통신 실패 >>>> : ");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receiveMsg;
    }
}
