package g.g.d.com.board;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import g.g.d.com.common.GogodaUtil;

// 게시글 데이터 받아오기
public class RboardJsonTask extends AsyncTask<String, Void, ArrayList<RboardListViewItem>> {

    String sendMsg, receiveMsg;

    // jsp 연결
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected ArrayList<RboardListViewItem> doInBackground(String... strings){
        System.out.println("RboardJsonTask doInBackground >>>>>>>>>>>>>>>>>>>");
        try {
            String bnum = strings[0];
            String str;
            URL url = new URL("http://"+ GogodaUtil.URL_PATH +"/springProject/rboard/all2/"+ bnum + ".ggd");
            System.out.println("url >>> : " + url);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("GET");
            InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(tmp);
            StringBuffer buffer = new StringBuffer();

            while ((str=reader.readLine()) !=null){
                buffer.append(str);
            }

            receiveMsg = buffer.toString();
            System.out.println(" >>>>>>>>>>>>>> : " + receiveMsg);

        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        ArrayList<RboardListViewItem> listViewItems = new ArrayList<RboardListViewItem>();
        try {
            System.out.println("result >>> : " + receiveMsg);
            JSONObject json = new JSONObject(receiveMsg);
            System.out.println(json);
            JSONArray jArray = json.getJSONArray("rboard");
            for(int i = 0; i< jArray.length(); i++){
                JSONObject obj = jArray.getJSONObject(i);
                listViewItems.add(new RboardListViewItem(obj));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listViewItems;
    }
}
