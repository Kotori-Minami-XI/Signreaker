package com.kotori;

import com.sun.jna.Native;

public class Kotori {
    static {
        //System.load("C:\\Users\\udrt\\Desktop\\repo\\zengkm\\Java\\unidbg\\unidbg-android\\src\\test\\resources\\example_binaries\\ttEncrypt");
        //System.load("libkotori");
        //System.loadLibrary("kotori");
    }

    public static native int dance();
    public static native int sing();
    public static native int greet(int a);
}
