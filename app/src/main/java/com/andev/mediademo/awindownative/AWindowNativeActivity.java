package com.andev.mediademo.awindownative;

import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.andev.mediademo.R;

import androidx.appcompat.app.AppCompatActivity;

public class AWindowNativeActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    //String url = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
    String url = "/mnt/sdcard/killer.mp4";

    Button tv_play;
    SurfaceView surfaceView;

    SurfaceHolder surfaceHolder;
    Surface surface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.awindow_native);

        tv_play = (Button)findViewById(R.id.tv_play);
        surfaceView = (SurfaceView)findViewById(R.id.surface_view);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        tv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(surface != null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            NativePlayer.playVideo(url, surface);
                        }
                    }).start();
                }
            }
        });


    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        surface = surfaceHolder.getSurface();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
