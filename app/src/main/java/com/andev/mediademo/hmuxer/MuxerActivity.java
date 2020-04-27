package com.andev.mediademo.hmuxer;

import android.app.Activity;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.andev.mediademo.R;

import java.io.IOException;
import java.nio.ByteBuffer;

public class MuxerActivity extends Activity {
    private static final String TAG = "MuxerActivity";

    private static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath();
    /** 原始的文件路径 **/
    private static String mp4FilePath = "/mnt/sdcard/killer.mp4";

    Button bt_start;
    private MediaExtractor mMediaExtractor;
    private MediaMuxer mMediaMuxer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muxer);

        bt_start = (Button) findViewById(R.id.bt_start);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void start() throws IOException{
        mMediaExtractor = new MediaExtractor();
        mMediaExtractor.setDataSource(mp4FilePath);

        int numTracks = mMediaExtractor.getTrackCount();

        Log.d(TAG, "numTracks : " + numTracks);
        int mVideoTrackIndex = -1;
        int framerate = 0;

        for (int i = 0; i < mMediaExtractor.getTrackCount(); i++) {
            MediaFormat format = mMediaExtractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (!mime.startsWith("video/")) {
                continue;
            }
            framerate = format.getInteger(MediaFormat.KEY_FRAME_RATE);

            Log.d(TAG, "framerate : " + framerate);

            mMediaExtractor.selectTrack(i);
            mMediaMuxer = new MediaMuxer(SDCARD_PATH + "/output.mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            mVideoTrackIndex = mMediaMuxer.addTrack(format);
            mMediaMuxer.start();
        }

        if (mMediaMuxer == null) {
            return;
        }

        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        info.presentationTimeUs = 0;
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
        int sampleSize = 0;

        while ((sampleSize = mMediaExtractor.readSampleData(buffer, 0)) > 0) {

            info.offset = 0;
            info.size = sampleSize;
            info.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
            info.presentationTimeUs += 1000 * 1000 / framerate;
            //info.presentationTimeUs = mMediaExtractor.getSampleTime();

            Log.d(TAG, "presentationTimeUs : " +  info.presentationTimeUs);
            int trackIndex = mMediaExtractor.getSampleTrackIndex();
            Log.d(TAG, "trackIndex : " +  trackIndex);

            mMediaMuxer.writeSampleData(mVideoTrackIndex, buffer, info);
            mMediaExtractor.advance();
        }

        mMediaExtractor.release();

        mMediaMuxer.stop();
        mMediaMuxer.release();

        Log.d(TAG, "finish");
    }

}
