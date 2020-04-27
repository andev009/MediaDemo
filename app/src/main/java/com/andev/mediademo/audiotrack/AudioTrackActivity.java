package com.andev.mediademo.audiotrack;

import android.app.Activity;
import android.media.AudioTrack;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.andev.mediademo.R;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AudioTrackActivity extends Activity {
    private static final String TAG = "AudioTrackActivity";

    Button bt_play;
    private String resPath = "/mnt/sdcard/vocal.pcm";

    AudioPlayer audioPlayer;

    private DataInputStream resDataInputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_track);

        bt_play = (Button) findViewById(R.id.bt_play);
        bt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlay();
            }
        });
    }


    private void startPlay() {
        File file = new File(resPath);
        if (file.exists()) {
            Log.d(TAG, "file.getAbsolutePath: " + file.getAbsolutePath());
        }
        try {
            resDataInputStream = new DataInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            Log.d(TAG, e.toString());
        }


        audioPlayer = new AudioPlayer();
        audioPlayer.startPlayer();

        Thread playThread = new Thread(new PlayerThread(), "PlayThread");
        playThread.start();
    }


    class PlayerThread implements Runnable {
        private byte[] samples;

        @Override
        public void run() {
            try {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                samples = new byte[audioPlayer.getMinBufferSize()];
                int readCount = 0;
                while (resDataInputStream.available() > 0) {
                    readCount= resDataInputStream.read(samples);
                    if (readCount == AudioTrack.ERROR_INVALID_OPERATION || readCount == AudioTrack.ERROR_BAD_VALUE) {
                        continue;
                    }

                    if (readCount != 0 && readCount != -1) {//一边播放一边写入语音数据
                        audioPlayer.play(samples, 0, readCount);
                    }
                }

                audioPlayer.stopPlayer();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
