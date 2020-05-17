package zd.cn.novipvideo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;

import zd.cn.novipvideo.R;
import zd.cn.novipvideo.activity.MainActivity;
import zd.cn.novipvideo.utils.DataManagerUtils;
import zd.cn.novipvideo.utils.ScreenUtil;
import zd.cn.novipvideo.utils.StatusBarUtils;

/**
 * 欢迎页面广告
 */
public class SplashActivity extends StatusBarActivity{

//    @Bind(R.id.splash_t)
//    TextView skipTv;//跳过

    @Bind(R.id.splash_pic)
    ImageView splashPic;
    @Bind(R.id.splash_zt)
    ImageView splashZt;

//    private CountDownTimer timer;//倒计时
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void initView() {
        splashPic.setImageResource(R.mipmap.cxsp_logo);
        splashZt.setImageResource(R.mipmap.cksp_zt);
        ScreenUtil.setRelativeLayoutParams(splashPic,200,200,200,0,0,0);
        ScreenUtil.setRelativeLayoutParams(splashZt,0,150,30,30,90,90);

        ScaleAnimation animation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,1, 0.5f);
        animation.setDuration(2000);
        splashPic.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(DataManagerUtils.getLogin() == -1){//第一次使用
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
//        skipTv.setOnClickListener(this);
//        ScreenUtil.setRelativeLayoutParams(skipTv,120,65,55,0,0,35);
//        ScreenUtil.setTextSize(skipTv,28);
//        //设置倒计时
//        setDownTime();
    }

//    private void setDownTime() {
//        // millisInFuture 倒计时的总时长 countDownInterval每次间隔时间
//        timer = new CountDownTimer(5500,1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                //倒计时显示 //防止内存 如果当前窗口不存，则销毁计时
//                if(!SplashActivity.this.isFinishing()){
//                    skipTv.setText("跳过 | "+millisUntilFinished/1000);
//                }
//            }
//            @Override
//            public void onFinish() {
//                //时间结束 跳转
//                startActivity(new Intent(SplashActivity.this,MainActivity.class));
//                finish();
//            }
//        }.start();
//    }

    /**
     * 关闭倒计时，防止内存泄漏
     */
//    public void cancleTimer(){
//        if(timer != null){
//            timer.cancel();
//            timer = null;
//        }
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        cancleTimer();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.splash_t:
//                cancleTimer();//取消定时
//                startActivity(new Intent(SplashActivity.this,MainActivity.class));
//                finish();
//                break;
//        }
//    }
}
