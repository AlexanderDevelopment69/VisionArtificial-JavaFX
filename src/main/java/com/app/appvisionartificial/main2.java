package com.app.appvisionartificial;

import org.opencv.core.Core;

public class main2 {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println(Core.VERSION);
    }
}
