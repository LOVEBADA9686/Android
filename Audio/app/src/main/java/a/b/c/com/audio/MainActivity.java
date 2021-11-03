package a.b.c.com.audio;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mp = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mp = MediaPlayer.create(this, R.raw.hare);
        mp.setLooping(false);
        mp.start();
    }

    protected void onDestroy() {
        if(mp.isPlaying()) {
            mp.stop();
            mp.release();
        }
        super.onDestroy();
    }
}