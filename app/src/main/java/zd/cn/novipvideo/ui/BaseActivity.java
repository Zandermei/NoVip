package zd.cn.novipvideo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import zd.cn.novipvideo.utils.ScreenUtil;
import zd.cn.novipvideo.utils.StatusBarCompat;
import zd.cn.novipvideo.utils.StatusBarUtils;

/**
 * Created by Administrator on 2019/7/15 0015.
 */

public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(beforeLayout()){
//            StatusBarUtils.setStatusBarStyle(this);//设置主体颜色
            setLayout();
            ButterKnife.bind(this);
            ScreenUtil.setScreenData(this);
            initView();
            setUpView();
        }else {
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    protected abstract void setUpView();//初始化数据

    protected abstract void initView();//初始化组件

    protected abstract void setLayout();//设置布局

    protected abstract boolean beforeLayout();
}
