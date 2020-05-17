package zd.cn.novipvideo.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import butterknife.Bind;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.utils.AppLog;
import zd.cn.novipvideo.utils.CommentUtl;

public class WelcomeActivity extends StatusBarActivity{

    @Bind(R.id.fragment_wel)
    ViewGroup fragmentWel;//广告容器
    @Bind(R.id.wel_img)
    ImageView welImg;//默认图片

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void initView() {
        context = this;
       // welImg.setImageResource(R.mipmap.ic_new_vod_bg);

    }

    @Override
    protected void setUpView() {

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            // 捕获back键，在展示广告期间按back键，不跳过广告
            if (fragmentWel.getVisibility() == View.VISIBLE) {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
