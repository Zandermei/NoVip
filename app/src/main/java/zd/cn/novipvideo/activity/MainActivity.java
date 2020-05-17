package zd.cn.novipvideo.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Toast;

import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import eu.long1.spacetablayout.SpaceTabLayout;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.fragment.AccountFragment;
import zd.cn.novipvideo.fragment.MovieFragment;
import zd.cn.novipvideo.fragment.NewsFragment;
import zd.cn.novipvideo.fragment.SearchFragment;
import zd.cn.novipvideo.fragment.VideoFragment;
import zd.cn.novipvideo.ui.StatusBarActivity;
import zd.cn.novipvideo.utils.AppLog;
import zd.cn.novipvideo.utils.DataManagerUtils;
import zd.cn.novipvideo.utils.ToastUtils;

public class MainActivity extends StatusBarActivity{
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.spaceTabLayout)
     SpaceTabLayout spaceTabLayout;

    private Context mContex;

    private List<Fragment> fragmentList = new ArrayList<>();
    private VideoFragment videoFragment;//视频
    private SearchFragment searchFragment;
    private AccountFragment accountFragment;
    private MovieFragment movieFragment;//电影

    private final int PREMISSION = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_mains);
    }

    @Override
    protected void initView() {
        mContex = this;
       // searchFragment = new SearchFragment();
        videoFragment = new VideoFragment();
        movieFragment = new MovieFragment();
        accountFragment = new AccountFragment();
      //  fragmentList.add(searchFragment);
        fragmentList.add(videoFragment);
        fragmentList.add(movieFragment);
        fragmentList.add(accountFragment);
        spaceTabLayout.initialize(viewPager,getSupportFragmentManager(),fragmentList);
    }

    @Override
    protected void setUpView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                    int isPer = ActivityCompat.checkSelfPermission(mContex, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    int isPhone = ActivityCompat.checkSelfPermission(mContex,Manifest.permission.READ_PHONE_STATE);
                    //int isIntnet = ActivityCompat.checkSelfPermission(mContex,Manifest.permission.INTERNET);
                    int isState = ActivityCompat.checkSelfPermission(mContex,Manifest.permission.ACCESS_NETWORK_STATE);
                    int isRead = ActivityCompat.checkSelfPermission(mContex,Manifest.permission.READ_EXTERNAL_STORAGE);
                    if(isPer != PackageManager.PERMISSION_GRANTED || isPhone != PackageManager.PERMISSION_GRANTED
                             || isState != PackageManager.PERMISSION_GRANTED
                            || isRead != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE
                                ,Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.READ_EXTERNAL_STORAGE},PREMISSION);
                    }
                }
            }
        },500);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initBugly();//收集bug信息上报
    }

    private void initBugly() {
        // 获取当前包名
        String packageName = mContex.getPackageName();
        // 获取当前进程名
        String processName =getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(mContex);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(mContex, "311526c204", false, strategy);
        CrashReport.setUserId(DataManagerUtils.getCurentUser());

    }
    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    long preTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
                long currentTime = new Date().getTime();
                // 如果时间间隔大于2秒，不处理
                if ((currentTime - preTime) > 2000) {
                    // 显示消息
                    ToastUtils.showCenter(this, "再按一次退出程序");
                    //更新时间
                    preTime = currentTime;
                    //截获事件，不再处理
                    return true;
                }else {
                    finish();
                }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isPer = false;
        if(grantResults.length > 0){
            for (int i=0;i<grantResults.length;i++){
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    isPer = true;
                }else {
                    isPer = false;
                }
            }
        }
        switch (requestCode){
            case PREMISSION:
                if(!isPer){
                    Toast.makeText(this,"权限不足",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
