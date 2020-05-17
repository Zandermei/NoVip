package zd.cn.novipvideo.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/3/1.
 */

public class ToastUtils {

    public static void showToast(Context context,String msg){

        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    public static void showCenter(Context context,String msg){
        Toast toast = Toast.makeText(context,msg,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }
    public static void showCenter1(Context context,String msg){
        Toast toast = Toast.makeText(context,msg,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
