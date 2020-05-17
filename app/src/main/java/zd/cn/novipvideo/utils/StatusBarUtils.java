package zd.cn.novipvideo.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import zd.cn.novipvideo.R;

/**
 * Created by Administrator on 2019/6/26 0026.
 */

public class StatusBarUtils {

    /**
     * 设置兼容4.4版本 状态栏颜色改变成和5.0效果差不多
     * 1、需要在values-19加上支持SystemWindows
     * 2、ToolBar添加属性 android:fitsSystemWindows="true"
     * 3、然后在加载完activity布局后调用该方法
     */

    public static void setStatusBarStyle(Activity context,int color){
        StatusBarCompat.compat(context,color);
        ViewGroup contentView =context.findViewById(Window.ID_ANDROID_CONTENT);
        //获取childView
        View mChildView = contentView.getChildAt(0);
        if(mChildView != null){
            ViewCompat.setFitsSystemWindows(mChildView,true);
        }

    }

}
