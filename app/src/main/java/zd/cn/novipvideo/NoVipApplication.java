package zd.cn.novipvideo;

import android.app.AliasActivity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alivc.conan.AlivcConan;
import com.aliyun.conan.AliVcPlayerConan;
import com.aliyun.player.AliPlayer;
import com.aliyun.player.AliPlayerFactory;

import com.tencent.smtt.sdk.QbSdk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import zd.cn.novipvideo.utils.AppLog;
import zd.cn.novipvideo.utils.CommentUtl;
import zd.cn.novipvideo.utils.DataManagerUtils;
import zd.cn.novipvideo.utils.OkHttpUtils;


/**
 * Created by Administrator on 2019/7/15 0015.
 */

public class NoVipApplication extends Application{

    private static Context mContex;
    @Override
    public void onCreate() {
        super.onCreate();
        mContex = this;
        OkHttpUtils.initEvent(mContex);
        DataManagerUtils.init(mContex);
        //初始化X5内核
        initX5Web();
    }


    private void initX5Web() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                AppLog.e("内核是否加载成功onViewInitFinished is " + arg0);
            }
            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);

    }

    public static Context getApplicationContex(){
        return mContex;
    }
}
