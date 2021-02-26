package com.kotori;

import com.sun.jna.Native;

public class KotoriJni {
    static {
        //System.load("C:\\Users\\udrt\\Desktop\\repo\\zengkm\\Java\\unidbg\\unidbg-android\\src\\test\\resources\\example_binaries\\libkotori.so");
        //System.load("libkotori");
        //System.loadLibrary("kotori");
    }

    public static void main(String[] args) {

    }

    public static native int dance();
    public static native void sing();
    public static native int greet(int a);
}
