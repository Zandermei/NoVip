package zd.cn.novipvideo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

/**
 * Created by Administrator on 2019/7/17 0017.
 */

public class DataManagerUtils {

    private String SAVE_NAME = "saveInfo";//保存的名称
    private static String USER_NAME = "userName";
    private static String LOGIN_CODE = "login";

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static DataManagerUtils dataManagerUtils;

    public DataManagerUtils(Context context){
        preferences = context.getSharedPreferences(SAVE_NAME,Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static synchronized void init(Context context){
        if(dataManagerUtils == null){
            dataManagerUtils = new DataManagerUtils(context);
        }
    }


    /**
     * 获取当前用户，用于崩溃标记
     * @return
     */
    public static String getCurentUser(){
        String name = preferences.getString(USER_NAME,"畅看视频");
        return name;
    }
    /**
     * 保存用户信息
     */
    public static void saveCurrentUser(String msg){
        editor.putString(USER_NAME,msg);
        editor.commit();
    }


    /**
     * 判断是否第一次使用
     * @return
     */
    public static int getLogin(){
        int name = preferences.getInt(LOGIN_CODE,-1);
        return name;
    }

    public static void saveLoginCode(int code){
        editor.putInt(LOGIN_CODE,code);
        editor.commit();
    }
}
