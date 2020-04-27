package com.andev.mediademo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.andev.mediademo.audioencodeh.AudioEncoderHActivity;
import com.andev.mediademo.audiotrack.AudioTrackActivity;
import com.andev.mediademo.awindownative.AWindowNativeActivity;
import com.andev.mediademo.ffmpegdecode.FFmpegDecodeMP3;
import com.andev.mediademo.hmuxer.MuxerActivity;
import com.andev.mediademo.learnjni.JniTest;
import com.andev.mediademo.record.AudioRecorderActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    /** 原始的文件路径 **/
    private static String mp3FilePath = "/mnt/sdcard/test.mp3";
    /** 解码后的PCM文件路径 **/
    private static String pcmFilePath = "/mnt/sdcard/pcm_test.pcm";

    TextView tv_audiorecorder;
    TextView tv_audiotrace;
    TextView tv_mp3decode;
    TextView tv_audioencode_h;
    TextView tv_muxer;
    TextView tv_awn_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_audiorecorder = ((TextView)findViewById(R.id.tv_audiorecorder));
        tv_audiotrace = ((TextView)findViewById(R.id.tv_audiotrace));
        tv_mp3decode = ((TextView)findViewById(R.id.tv_mp3decode));
        tv_audioencode_h = ((TextView)findViewById(R.id.tv_audioencode_h));
        tv_muxer = ((TextView)findViewById(R.id.tv_muxer));
        tv_awn_play = ((TextView)findViewById(R.id.tv_awn_play));

        ((TextView)findViewById(R.id.tv_text)).setText(JniTest.getStringFromC());

        ((TextView)findViewById(R.id.tv_text_dynamic)).setText("ffmpeg version:" +
                FFmpegDecodeMP3.getFFmpegVersion());

        Log.d(TAG, JniTest.getStringFromC());

        JniTest jniTest = new JniTest();

        int[] array = new int[] { 0, 0 };

        jniTest.testArray(array);

        Log.d(TAG, array[0] + "  " + array[1]);

        tv_audiorecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AudioRecorderActivity.class));
            }
        });

        tv_audiotrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AudioTrackActivity.class));
            }
        });

        tv_mp3decode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FFmpegDecodeMP3.decode(mp3FilePath, pcmFilePath);
            }
        });

        tv_audioencode_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AudioEncoderHActivity.class));
            }
        });

        tv_muxer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MuxerActivity.class));
            }
        });

        tv_awn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AWindowNativeActivity.class));
            }
        });
    }
}
