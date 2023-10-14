package com.example.tugasfadil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//            // Hilangkan bilah tindakan (action bar) jika ada
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


            setContentView(R.layout.activity_splash_screen);
            VideoView videoView = findViewById(R.id.videoView);
            videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.splassscreennew);
            videoView.start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.layout_in, R.anim.layout_out);

            }
        }, 7000);
    }
}