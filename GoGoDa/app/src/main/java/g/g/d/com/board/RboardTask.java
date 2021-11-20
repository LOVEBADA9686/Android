package g.g.d.com.board;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import g.g.d.com.common.GogodaUtil;

// 게시글 데이터 서버에 보내기
public class RboardTask extends AsyncTask<String, Void, String> {

    String sendMsg, receiveMsg;

    // jsp 연결
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected String doInBackground(String... strings){
        System.out.println("RegisterActivity doInBackground >>>>>>>>>>>>>>>>>>>");
        String bnum = strings[0];
        try {
            String str;
            // Android/Android/androidDB.jsp  board/boardInsert2.ggd
            URL url = new URL("http://"+ GogodaUtil.URL_PATH +"/springProject/rboard/rboardInsert.ggd");
            System.out.println("url >>> : " + url);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8);

            // insert 한 데이터
            JSONObject object = new JSONObject();
            object.put("bnum", bnum);
            object.put("rbname", strings[1]);
            object.put("rbcontent", strings[2]);
            object.put("rbpw", strings[3]);

            sendMsg = object.toString();
            osw.write(sendMsg);
            osw.flush();

            if (conn.getResponseCode() == conn.HTTP_OK){
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();

                while ((str=reader.readLine()) !=null){
                    buffer.append(str);
                }

                receiveMsg = buffer.toString();
            }else{
                // 연결 실패
                Log.e("RBoardTask","Response Code is not ok");
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
            Log.e("RboardTask",e.toString());
        }catch (IOException e){
            e.printStackTrace();
            Log.e("RboardTask",e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("RboardTask",e.toString());
        }
        return receiveMsg;
    }
}
