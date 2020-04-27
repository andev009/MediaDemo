package com.andev.mediademo.learnjni;

public class JniTest {
    static {
        System.loadLibrary("ndkffmpeg");
    }

    public static native String getStringFromC();


    public native int testArray(int[] metaArray);

}