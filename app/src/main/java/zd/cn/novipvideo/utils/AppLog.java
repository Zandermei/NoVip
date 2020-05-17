package zd.cn.novipvideo.utils;

import android.util.Log;

/**
 * Created by Administrator on 2019/7/15 0015.
 */

public class AppLog {

    private static boolean DEBUG = true;

    public static void e(String ss){
        if(DEBUG){
            Log.e("----------",ss);
        }
    }
    }
