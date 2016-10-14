package com.cqutprint.shundai.utils;



/**
 * Created by shang guangneng
 */
public class Log {

    private Log() {}

    //日志打印开关
    public static boolean isLogEnable = true;

    /**
     * Debug
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            android.util.Log.d(tag, rebuildMsg(stackTraceElement, msg));
        }
    }

    /**
     * Information
     *
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            android.util.Log.i(tag, rebuildMsg(stackTraceElement, msg));
        }
    }

    /**
     * Information
     * show method name
     * @param tag
     */
    public static void i(String tag) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            android.util.Log.i(tag, rebuildMsg(stackTraceElement,""));
        }
    }

    /**
     * Verbose
     *
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            android.util.Log.v(tag, rebuildMsg(stackTraceElement, msg));
        }
    }

    public static void w(String tag, String msg) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            android.util.Log.w(tag, rebuildMsg(stackTraceElement, msg));
        }
    }


    public static void e(String tag, String msg) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            android.util.Log.e(tag, rebuildMsg(stackTraceElement, msg));
        }
    }

    /**
     * 格式化msg形式
     *
     * @param msg
     * @return
     */
    private static String rebuildMsg(StackTraceElement stackTraceElement, String msg) {
        StringBuffer sb = new StringBuffer();
        sb.append(" <--GuangNeng--> "+stackTraceElement.getFileName());
        sb.append(" (");
        sb.append(stackTraceElement.getLineNumber());
        sb.append(") ");
        sb.append(stackTraceElement.getMethodName());
        sb.append(" : ");
        sb.append(msg);
        return sb.toString();
    }

}
