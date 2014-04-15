package edu.fudan.se.asof.util;

import java.util.Arrays;

/**
 * Created by Dawnwords on 2014/4/15.
 */
public class Log {

    private static final String DEBUG_TAG = "Dawnwords";

    public static void debug(String message) {
        StackTraceElement element = new Throwable().getStackTrace()[2];
        String className = element.getClassName();
        String methodName = element.getMethodName();
        int lineNumber = element.getLineNumber();
        android.util.Log.i(DEBUG_TAG, String.format("%s:%s(%d):%s", className, methodName, lineNumber, message));
    }

    public static void debug(float message){
        debug("" + message);
    }

    public static void debug(int message){
        debug("" + message);
    }

    public static void debug(int[] message){
        debug(Arrays.toString(message));
    }

    public static void debug(String[] message){
        debug(Arrays.toString(message));
    }

    public static void debug(double[] message){
        debug(Arrays.toString(message));
    }

    public static void debug(byte[] message){
        debug(Arrays.toString(message));
    }

    public static void debug() {
        debug("");
    }
}
