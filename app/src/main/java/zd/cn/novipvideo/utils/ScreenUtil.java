package zd.cn.novipvideo.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2019/7/15 0015.
 */

public class ScreenUtil {

    public static int screenWidth = 0;
    public static int screenHeight = 0;
    public static int designScreenWidth = 720;//
    public static int designScreenHeight = 1280;

    public static void setScreenData(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        return screenHeight;
    }


    public static int dip2px(Context context, float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static int px2dip(Context context,float PxValue){
        final float scale  = context.getResources().getDisplayMetrics().density;
        return (int) (PxValue / scale + 0.5f);

    }

    public static int sp2px(Context context,float sp){
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }



    public static int getRealWidth(int designWidth){
        return designWidth * screenWidth /designScreenWidth;
    }


    public static int getRealHeight(int picWidth,int picHeight,int currentWidth){
        return picHeight * currentWidth / picWidth;

    }
    public static void setLinearLayoutParams(View view, int width, int height, int topMargin,
                                             int bottpmMargin, int leftMargin, int rightMargin){

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();

        if(width > 0 ){
            params.width = getRealWidth(width);
        }
        if(height > 0){
            params.height = getRealWidth(height);
        }
        if(topMargin > 0){
            params.topMargin = getRealWidth(topMargin);
        }
        if(bottpmMargin > 0){
            params.bottomMargin = getRealWidth(bottpmMargin);
        }
        if(leftMargin > 0){
            params.leftMargin = getRealWidth(leftMargin);
        }
        if(rightMargin > 0){
            params.rightMargin = getRealWidth(rightMargin);
        }
    }
    public static void setRelativeLayoutParams(View view,int width,int height,int topMargin,
                                               int bottpmMargin,int leftMargin,int rightMargin){

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();

        if(width > 0 ){
            params.width = getRealWidth(width);
        }
        if(height > 0){
            params.height = getRealWidth(height);
        }
        if(topMargin > 0){
            params.topMargin = getRealWidth(topMargin);
        }
        if(bottpmMargin > 0){
            params.bottomMargin = getRealWidth(bottpmMargin);
        }
        if(leftMargin > 0){
            params.leftMargin = getRealWidth(leftMargin);
        }
        if(rightMargin > 0){
            params.rightMargin = getRealWidth(rightMargin);
        }

    }

    public static void setFragmeLayoutParams(View view,int width,int height,int topMargin,
                                             int bottpmMargin,int leftMargin,int rightMargin){

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();

        if(width > 0 ){
            params.width = getRealWidth(width);
        }
        if(height > 0){
            params.height = getRealWidth(height);
        }
        if(topMargin > 0){
            params.topMargin = getRealWidth(topMargin);
        }
        if(bottpmMargin > 0){
            params.bottomMargin = getRealWidth(bottpmMargin);
        }
        if(leftMargin > 0){
            params.leftMargin = getRealWidth(leftMargin);
        }
        if(rightMargin > 0){
            params.rightMargin = getRealWidth(rightMargin);
        }

    }

    public static void setTextSize(TextView tv, int size){
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,getRealWidth(size));
    }

    public static void setPadding(View view, int topPadding, int bottomPadding, int leftPadding, int rightPadding){
        view.setPadding(getRealWidth(leftPadding),getRealWidth(rightPadding),getRealWidth(topPadding),getRealWidth(bottomPadding));
    }
}
