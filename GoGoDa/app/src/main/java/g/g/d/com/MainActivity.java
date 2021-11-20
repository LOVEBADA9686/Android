package g.g.d.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.graphics.drawable.ColorDrawable;


import g.g.d.com.board.BoardListView;
import g.g.d.com.map.KakaoMap;
import g.g.d.com.vo.Document;
import g.g.d.com.R;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    Button emotionbtn;
    EditText emotioninput;
    Button boardbtn;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private void getPermission(){

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                1000);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(SaveSharedPreference.getUserName(MainActivity.this).length() == 0) {
            // call Login

            Intent loginIntent = new Intent(MainActivity.this, Login.class);
            startActivity(loginIntent);
            this.finish();
        } else {
            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show();

        }

        // 권한이 허용되어있지않다면 권한요청
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        // 권한이 허용되어있다면 다음 화면 진행
        else {

        }
        // 음성인식을 위한 인텐트 생성
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        ///// 호출하는 현재 패키지명 지정
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        /////  음성인식 언어 설정
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        // 음성인식 객체 생성과 리스너 설정
        SpeechRecognizer mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(recognitionListener);
        emotioninput = (EditText) findViewById(R.id.keyword2);
        emotioninput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "음성인식 중입니다...", Toast.LENGTH_SHORT).show();
                mRecognizer.startListening(intent);
            }
        });


        emotionbtn = (Button) findViewById(R.id.emotionbtn);
        emotionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emotioninput = (EditText) findViewById(R.id.keyword2);

                intent = new Intent(MainActivity.this, Emotion.class);
                intent.putExtra("text", emotioninput.getText().toString());
                startActivity(intent);
                //                   this.finish();

            }
        });

        boardbtn = (Button) findViewById(R.id.boardbtn);
        boardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent boardIntent = new Intent(MainActivity.this, BoardListView.class);
                startActivity(boardIntent);
            }
        });












    }

    // 음성인식 리스너 객체 생성
    private RecognitionListener recognitionListener = new RecognitionListener() {
        // 사용자가 말할 때까지 대기하는 상태
        @Override
        public void onReadyForSpeech(Bundle bundle) {
  //          button.setText("음성인식 대기 중...");
 //           textView.setText("");
        }

        // 사용자가 말하기 시작할 때
        @Override
        public void onBeginningOfSpeech() {
        }

        // 음성의 사운드 수준이 변할 때
        @Override
        public void onRmsChanged(float v) {
        }

        // 여러 소리가 들려질 때
        @Override
        public void onBufferReceived(byte[] bytes) {
        }

        // 사용자가 말을 마칠 때
        @Override
        public void onEndOfSpeech() {
   //         button.setText("버튼을 누르고 말하세요!");
        }

        // 네트워크 또는 음성인식 에러
        @Override
        public void onError(int i) {
 //           textView.setText("말이 없네요...");
        }

        // 음성인식 결과
        @Override
        public void onResults(Bundle bundle) {
            ///// 음성인식 결과를 문자형(String) 속성을 갖는 ArrayList에 할당
            ArrayList<String> mResult = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            /////  ArrayList 크기만큼 문자 배열 할당
            String[] rs = new String[mResult.size()];
            ///// 음성인식 결과를 문자열로 변환
            mResult.toArray(rs);

            ///// 음성인식된 문자 출력
            emotioninput.setText(rs[0]);
        }

        // 음성인식이 부분적으로 된 경우
        @Override
        public void onPartialResults(Bundle bundle) {
        }

        // 방생 에정 이벤트 설정
        @Override
        public void onEvent(int i, Bundle bundle) {
        }
    };


}
