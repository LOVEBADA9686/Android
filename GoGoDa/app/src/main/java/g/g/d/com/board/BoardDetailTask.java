package g.g.d.com.board;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import g.g.d.com.common.GogodaUtil;

public class BoardDetailTask extends AsyncTask<String, Void, BoardItem> {

    String sendMsg, receiveMsg;

    @Override
    protected BoardItem doInBackground(String... params) {
        BoardItem result = null;
        System.out.println("BoardDetailTask doInBackground >>>>>>>>>>>>>>>>>>>");
        try {
            String bnum = (String)params[0];
            URL url = new URL("http://" + GogodaUtil.URL_PATH +"/springProject/board/boardDetail2.ggd?bnum="+bnum);
            System.out.println("url >>> : " + url);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("GET");

            String str;
            InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuffer buffer = new StringBuffer();

            while ((str=reader.readLine()) !=null){
                buffer.append(str);
            }

            receiveMsg = buffer.toString();
            JSONObject jsonObject = new JSONObject(receiveMsg);

            result = new BoardItem(jsonObject);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
