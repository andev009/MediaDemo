package com.andev.mediademo.awindownative;

public class NativePlayer {

    static {
        System.loadLibrary("ndkffmpeg");
    }

    public static native int playVideo(String url, Object surface);
}
