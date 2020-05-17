package zd.cn.novipvideo.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zdd on 2019/6/26 0026.
 * 兼容4.4状态栏 状态栏颜色改变成和5.0效果差不多
 */

public class StatusBarCompat {
    private static final int PARAMS = -1;//定义一个不可能的参数（用不到的参数）
    private static final int DEFAULT_COLOR = Color.parseColor("#20000000");//定义一个默认的参数


    /**
     * 开始设置状态栏
     */
    public static void compat(Activity activity,int statusColor){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){ //当版本大于5.0时
            if(statusColor != PARAMS){
                activity.getWindow().setStatusBarColor(statusColor);
            }
            return;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){

            int color = DEFAULT_COLOR;
            //获取状态栏内容布局
            ViewGroup contentView = activity.findViewById(android.R.id.content);
            if(statusColor != PARAMS){
                color = statusColor;
            }
            //创建自己的view
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView,lp);
        }

    }

    private static int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId =activity.getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourceId > 0){
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
