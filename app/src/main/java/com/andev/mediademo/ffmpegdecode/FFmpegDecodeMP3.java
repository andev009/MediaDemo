package com.andev.mediademo.ffmpegdecode;

public class FFmpegDecodeMP3 {
    static {
        System.loadLibrary("ndkffmpegdecodemp3");
    }

    public native static void decode(String mp3FilePath, String pcmFilePath);

    public native static String getFFmpegVersion();
}
