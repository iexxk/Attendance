package com.xuan.attendance.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/1/21.
 */
public class LogUtils {
    public static final String CACHE_DIR_NAME = "dPhoneLog";

    public static boolean isDebugModel = true;// 是否输出日志


    public static void d(final String tag, final String msg) {
        if (isDebugModel) {
            Log.d(tag, "-->> " + msg);
        }
    }
    public static void e(final String tag, final String msg) {
        if (isDebugModel) {
            Log.e(tag, "-->> " + msg);
        }
    }
    public static void v(final String tag, final String msg) {
        if (isDebugModel) {
            Log.v(tag, "-->> " + msg);
        }
    }
    /**
     * args 标记 str 内容
     * */
    public static void systemerr(String args, String str) {
        if (isDebugModel) {
            System.err.println(args + "\t" + str);
        }
    }
    /**
     *  显示Toast 时间短
     * @param context上下文
     * @param str 显示的内容
     */
    public static void ToastmakeText(Context context,String str) {
        if (isDebugModel) {
            Toast.makeText(context, str, Toast.LENGTH_LONG).show();
        }
    }
    /**
     * @return 获取代码当前的行数
     */
    public static String getLineInfo() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getFileName() + ":Line" + ste.getLineNumber();
    }

}
