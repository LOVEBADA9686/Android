package a.b.c.com.video;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri uri = Uri.parse("android.resource://com.example.video/" + R.raw.fountain_night);

        VideoView videoView = (VideoView) findViewById(R.id.videoview);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setVisibility(View.VISIBLE);
    }
}