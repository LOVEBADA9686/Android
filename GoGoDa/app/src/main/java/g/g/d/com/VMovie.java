package g.g.d.com;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class VMovie extends AppCompatActivity {

    String clientId = "0WNBUK1f60vTSQAnUor_";
    String clientSecret = "hleXArecKC";
    String sTitle;
    private String mJsonString;
    private static String IP_ADDRESS = "https://openapi.naver.com/v1/search/movie.json?query=";
    private static String TAG = "Movie :";
    private WebView mWebView; // 웹뷰 선언
    private WebSettings mWebSettings; //웹뷰세팅
    private String linkUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v_movie);

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
        mWebSettings = mWebView.getSettings(); //세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        mWebSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        mWebSettings.setSupportZoom(false); // 화면 줌 허용 여부
        mWebSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            LOGDT data = extras.getParcelable("data");
            if (data != null) {
                sTitle = data.mid;

                Log.d("VMovie", "sTitle - " + sTitle);
                mWebView.loadUrl(sTitle);

                /**
                Thread thread = new Thread() {
                   public void run() {
                        String text = null;
                        try {
                            text = URLEncoder.encode(sTitle, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException("검색어 인코딩 실패",e);
                        }

                        String apiURL = IP_ADDRESS + text;    // json 결과

                        Map<String, String> requestHeaders = new HashMap<>();
                        requestHeaders.put("X-Naver-Client-Id", clientId);
                        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
                        String responseBody = get(apiURL,requestHeaders);

                        parseData(responseBody);
                    }
                };
                thread.start();**/
            }
        }


    }
/**
    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }

        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    private  void parseData(String responseBody) {
        String link;
        JSONObject jsonObject = null;
  //      Log.d("VMovie", "response - " + responseBody);
        try {
            jsonObject = new JSONObject(responseBody);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            Log.d("VMovie", "response - " + responseBody);
            Log.d("VMovie", "jasonArray - " + jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                link = item.getString("link");
                linkUrl = link;
                Log.d("VMovie", "link - " + linkUrl);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }**/

}