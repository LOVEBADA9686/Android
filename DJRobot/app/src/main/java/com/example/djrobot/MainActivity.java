package com.example.djrobot;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout cl;
    ImageView iv;
    private TextToSpeech tts;
    Intent intent;
    SpeechRecognizer mRecognizer;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cl = (ConstraintLayout)findViewById(R.id.cl);
        iv = (ImageView)findViewById(R.id.iv);

        mp = new MediaPlayer();

        // TTS 객체 생성
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    ///// 언어 선택
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        // 앱 처음 실행 시, 음성인식 허용 여부 확인 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            /////  음성인식이 허용되어 있지 않으면 허용 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }

        // 음성인식을 위한 인텐트 생성
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        ///// 호출하는 현재 패키지명 지정
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        /////  음성인식 언어 설정
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        // 음성인식 객체 생성과 리스너 설정
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(recognitionListener);

        // 볼륨 조절
        ///// 볼륨 조절 또는 벨소리 모드 설정 객체 생성
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        ///// 음악 재생을 위한 오디오 볼륨(STREAM_MUSIC) 모드에 대한 최대 볼륨 수 반환
        int amStreamMusicMaxVol = am.getStreamMaxVolume(am.STREAM_MUSIC);
        ///// 음악 재생을 위한 오디오 볼륨 모드로 오디오 볼륨을 최대로 설정
        am.setStreamVolume(am.STREAM_MUSIC, amStreamMusicMaxVol, 0);
    }

    // 음성인식 리스너 객체 생성
    private RecognitionListener recognitionListener = new RecognitionListener() {
        // 사용자가 말할 때까지 대기하는 상태
        @Override
        public void onReadyForSpeech(Bundle bundle) {
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
        }

        // 네트워크 또는 음성인식 에러
        @Override
        public void onError(int i) {
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

            ///// 음성인식 문자에 해당하는 노래 배경 이미지 출력 및 노래 재생
            if (rs[0].equals("학교종")) {
                iv.setImageResource(R.drawable.img_schoolbell);
                mp = MediaPlayer.create(MainActivity.this, R.raw.schoolbell);
                mp.start();
            } else if (rs[0].equals("산토끼")) {
                iv.setImageResource(R.drawable.img_hare);
                mp = MediaPlayer.create(MainActivity.this, R.raw.hare);
                mp.start();
            } else if (rs[0].equals("코끼리 아저씨")) {
                iv.setImageResource(R.drawable.img_elephant);
                mp = MediaPlayer.create(MainActivity.this, R.raw.elephant);
                mp.start();
            }
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

    // 룰렛 이미지가 터치될 때 실행되는 콜백 함수
    public void recognizeVoice(View v) {
        ///// 노래 재생 중이면 중지
        if (mp.isPlaying()) {
            mp.stop();
            mp.release();
        }

        ///// 문자를 읽어 사은드로 출력
        tts.speak("어떤 노래를 들려드릴까요",TextToSpeech.QUEUE_FLUSH, null);
        ///// 음성인식 리스너 시작
        mRecognizer.startListening(intent);
    }

    // 앱이 종료될 때 실행
    @Override
    protected void onDestroy() {
        ///// 액티비티 종료
        super.onDestroy();

        ///// TTS 객체를 삭제
        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }

        ///// 노래 재생 중이면 종료
        if (mp.isPlaying()) {
            mp.stop();
            mp.release();
        }
    }
}
