package g.g.d.com.board;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import g.g.d.com.common.GogodaUtil;

public class RegisterTask extends AsyncTask<BoardItem, Void, String> {

    byte[] sendMsg;
    String receiveMsg;

    // jsp 연결
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected String doInBackground(BoardItem... boardItems){
        System.out.println("RegisterTask doInBackground >>>>>>>>>>>>>>>>>>>");
        try {
            String str;
            // Android/Android/androidDB.jsp  board/boardInsert2.ggd
            URL url = new URL("http://" + GogodaUtil.URL_PATH +"/springProject/board/boardInsert.ggd");
            System.out.println("url >>> : " + url);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+ BoardItem.boundary);
            conn.setRequestMethod("POST");
            //OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            OutputStream outputStream = conn.getOutputStream();

            // insert 한 데이터
            //sendMsg = "bsubject=" + strings[0] + "&bname=" + strings[1] + "&bcontent=" + strings[2] + "&bpw=" + strings[3] + "&bfile=" + strings[4];
            BoardItem boardItem = boardItems[0];
            sendMsg = boardItem.export();

            /*
            int length = sendMsg.length;
            int offset = 0;
            for (;offset<length && offset + 1024 < length;offset+=1024) {
                outputStream.write(sendMsg, offset, 1024);
            }
            int sendLength = length - offset;
            outputStream.write(sendMsg, offset, sendLength);
            */
            outputStream.write(sendMsg);
            outputStream.flush();


            int code = conn.getResponseCode();
            if (code == conn.HTTP_OK){
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();

                while ((str=reader.readLine()) !=null){
                    buffer.append(str);
                }

                receiveMsg = buffer.toString();
            }else{
                // 연결 실패
                InputStreamReader tmp = new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();

                while ((str=reader.readLine()) !=null){
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                Log.e("REGISTER TASK", "Fail to Connect!");
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
            Log.e("REGISTER TASK", e.getMessage());
        }catch (IOException e){
            e.printStackTrace();
            Log.e("REGISTER TASK", e.getMessage());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Log.e("REGISTER TASK", e.getMessage());
        }
        return receiveMsg;
    }
}
