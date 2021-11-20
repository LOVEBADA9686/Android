package g.g.d.com.member;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.util.zip.GZIPOutputStream;

import g.g.d.com.R;
import g.g.d.com.common.GogodaUtil;

public class WebViewActivity extends AppCompatActivity {
    private WebView browser;
    class MyJavaScriptInterface{
        @JavascriptInterface
        //unused/
        public void processDATA(String data){
            Bundle extra = new Bundle();
            Intent intent = new Intent();
            extra.putString("data", data);
            intent.putExtras(extra);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        browser = (WebView) findViewById(R.id.webView);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.addJavascriptInterface(new MyJavaScriptInterface(), "Android");

        browser.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                browser.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });
        browser.loadUrl("http://" + GogodaUtil.URL_PATH +"/springProject/mem/androidDaum.ggd");
    }
}
