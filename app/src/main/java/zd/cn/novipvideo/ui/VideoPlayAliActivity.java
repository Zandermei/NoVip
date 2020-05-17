package zd.cn.novipvideo.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.aliyun.player.AliPlayer;
import com.aliyun.player.AliPlayerFactory;
import com.aliyun.player.IPlayer;
import com.aliyun.player.nativeclass.MediaInfo;
import com.aliyun.player.source.UrlSource;
import com.pnikosis.materialishprogress.ProgressWheel;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.Bind;


import zd.cn.novipvideo.R;
import zd.cn.novipvideo.model.MovModel;
import zd.cn.novipvideo.utils.AppLog;
import zd.cn.novipvideo.utils.StatusBarUtils;
import zd.cn.novipvideo.view.ImageTitleBar;
import zd.cn.novipvideo.widget.AliyunScreenMode;
import zd.cn.novipvideo.widget.AliyunVodPlayerView;
import zd.cn.novipvideo.widget.constants.PlayParameter;
import zd.cn.novipvideo.widget.control.ControlView;
import zd.cn.novipvideo.widget.gesturedialog.AlivcShowMoreDialog;
import zd.cn.novipvideo.widget.gesturedialog.BrightnessDialog;
import zd.cn.novipvideo.widget.more.AliyunShowMoreValue;
import zd.cn.novipvideo.widget.more.ShowMoreView;
import zd.cn.novipvideo.widget.more.SpeedValue;
import zd.cn.novipvideo.widget.utils.FixedToastUtils;
import zd.cn.novipvideo.widget.utils.NetWatchdog;
import zd.cn.novipvideo.widget.utils.ScreenUtils;


public class VideoPlayAliActivity extends StatusBarActivity{

    @Bind(R.id.alivc_video)
    AliyunVodPlayerView playerView;//视频播放容器
    @Bind(R.id.title_ali)
    ImageTitleBar titleBar;
    @Bind(R.id.progress)
    ProgressWheel progress;

    private String intentVideoUrl;//视频url
    private String intentImgUrl;//封面url
    private String intentTitle = "视频播放";//默认标题
    private AlivcShowMoreDialog showMoreDialog;
    private AliyunScreenMode aliyunScreenMode = AliyunScreenMode.Small;
    private List<MovModel.VodUrl> vodUrlList;
    private int currentVolum;//默认当前音量
    private AudioManager audioManager;//Audio管理器，用了控制音量

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setLayout() {
        StatusBarUtils.setStatusBarStyle(this,getResources().getColor(R.color.toorBar));
        setContentView(R.layout.activity_play_alivideo);
    }

    @Override
    protected void initView() {
        //设置AliyunVodPlayerView
        vodUrlList = new ArrayList<>();
        playerView.setKeepScreenOn(true);//保持屏幕常亮
        PlayParameter.PLAY_PARAM_TYPE = "localSource";
        intentVideoUrl = getIntent().getStringExtra("url");
        intentTitle = getIntent().getStringExtra("title");
        if(TextUtils.isEmpty(intentVideoUrl)){
            return;
        }
        dowill();
        //设置主题
        playerView.setTheme(AliyunVodPlayerView.Theme.Red);
//        playerView.setLocalSource(setPlaySource());//设置视频播放url
        playerView.setAutoPlay(true);//是否自动播放
        playerView.setOnPreparedListener(new MyPrepareListener());//视频准备就绪触发
        playerView.setOnCompletionListener(new MyOnCompletionListener());//视频播放完成触发
        playerView.setOrientationChangeListener(new MyOrientionChangeListener());//屏幕方向监听 是否全屏
        playerView.setOnScreenBrightness(new MyOnScreenBrigtness());//当前屏幕亮度
        playerView.setOnShowMoreClickListener(new MyOnShowMoreListener());//显示更多菜单
        playerView.setScreenBrightness(BrightnessDialog.getActivityBrightness(this));
        playerView.setOnShowMoreClickListener(new MyOnShowMoreListener());
        playerView.setOnVolumClickListener(new MyOnVolumListener());
        initAudio();//初始化音量播放器
        //设置视频标题
        playerView.setVideoTitle(intentTitle);
    }

    private void dowill() {
        //分割字符创
        Pattern pattern = Pattern.compile("\\$\\$\\$");
        Pattern pattern1 = Pattern.compile("#");
        String[] url = pattern.split(intentVideoUrl);//只取第一类数据
        String[] urlRelay = pattern1.split(url[0]);
        for (int j = 0; j < urlRelay.length; j++) {
            MovModel.VodUrl vodUrl = new MovModel.VodUrl();
            vodUrl.setVod_play_url(urlRelay[j].substring(urlRelay[j].indexOf("$") + 1, urlRelay[j].length()));
            vodUrl.setVod_play_title(urlRelay[j].substring(0, urlRelay[j].indexOf("$")));
            vodUrlList.add(vodUrl);
        }

        UrlSource source = new UrlSource();
        source.setUri(vodUrlList.get(0).getVod_play_url());
        playerView.setLocalSource(source);


    }

    private void initAudio() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        currentVolum = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolum,AudioManager.FLAG_PLAY_SOUND);
    }

    @Override
    protected void setUpView() {
        titleBar.setLeftImgVisibility(View.VISIBLE);
        if(TextUtils.isEmpty(intentTitle)){
            titleBar.setTitle("畅看视频");
        }else {
            titleBar.setTitle(intentTitle);
        }
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int type = NetWatchdog.getCurrentNetType(this);
        if(type == -1){
            progress.setVisibility(View.GONE);
        }else {
            progress.setVisibility(View.VISIBLE);
        }
    }

    //播放视频方法
    private UrlSource setPlaySource() {
        UrlSource urlSource = new UrlSource();
        urlSource.setUri(intentVideoUrl);
        urlSource.setTitle(intentTitle);
        return urlSource;
    }


    /**
     * 手机方向改变是回调
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updatePlayerViewMode();
    }

    /**
     * 防止手机锁屏，再次打开，，视频全屏 出现标题栏
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        updatePlayerViewMode();
    }

    private void updatePlayerViewMode() {
        if (playerView != null) {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                //转为竖屏了。
                //显示状态栏
                this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                //隐藏自定标题栏
                titleBar.setVisibility(View.VISIBLE);
                playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                //设置view的布局，宽高之类
                RelativeLayout.LayoutParams aliVcVideoViewLayoutParams = (RelativeLayout.LayoutParams) playerView
                        .getLayoutParams();
                aliVcVideoViewLayoutParams.height = (int) (ScreenUtils.getWidth(this) * 9.0f / 16);
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //转到横屏了。
                //隐藏状态栏
                titleBar.setVisibility(View.GONE);
                if (!isStrangePhone()) {
                    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
                //设置view的布局，宽高
                RelativeLayout.LayoutParams aliVcVideoViewLayoutParams = (RelativeLayout.LayoutParams) playerView
                        .getLayoutParams();
                aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            }
        }
    }

    /**  一些监听接口  开始    */

    private class MyPrepareListener implements AliPlayer.OnPreparedListener{
        @Override
        public void onPrepared() {//视频准备就绪  隐藏等待进度条
            progress.setVisibility(View.GONE);
        }
    }

    //视频播放结束触发
    private class MyOnCompletionListener implements IPlayer.OnCompletionListener{
        @Override
        public void onCompletion() {
            if(playerView != null){
                playerView.setAutoPlay(false);
                playerView.onStop();
            }
        }
    }




    //屏幕方向监听
    private class MyOrientionChangeListener implements AliyunVodPlayerView.OnOrientationChangeListener{
        @Override
        public void orientationChange(boolean from, AliyunScreenMode currentMode) {
          if(showMoreDialog != null){
              if(currentMode == AliyunScreenMode.Small){
                  showMoreDialog.dismiss();
                  aliyunScreenMode = currentMode;
              }
          }
        }
    }

    //当前屏幕亮度控制
    private class MyOnScreenBrigtness implements AliyunVodPlayerView.OnScreenBrightnessListener{
        @Override
        public void onScreenBrightness(int brightness) {
            //设置屏幕亮度
            setWidowBright(brightness);
            AppLog.e("当前亮度："+brightness);
          if(playerView != null){
              playerView.setScreenBrightness(brightness);
          }
        }
    }

    /**
     * 设置屏幕亮度
     * @param brightness
     */
    private void setWidowBright(int brightness) {
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.screenBrightness = brightness / 255.0f;
        window.setAttributes(layoutParams);
    }

    //点击更多按钮
    private class MyOnShowMoreListener implements ControlView.OnShowMoreClickListener{
        @Override
        public void showMore() {
            showMoreDialog = new AlivcShowMoreDialog(VideoPlayAliActivity.this);
            AliyunShowMoreValue moreValue = new AliyunShowMoreValue();
            moreValue.setVolume((int) playerView.getCurrentVolume());//设置 dialog页面的声音进度条
            moreValue.setScreenBrightness(playerView.getScreenBrightness());//设置屏幕亮度进度条
            //moreValue.setSpeed(); //当前倍速播放
            ShowMoreView showMoreView = new ShowMoreView(VideoPlayAliActivity.this,moreValue);
            showMoreDialog.setContentView(showMoreView);
            showMoreDialog.show();
            /**
             * 初始化亮度
             */
            if(playerView != null){
                showMoreView.setBrightness(playerView.getScreenBrightness());
            }
            showMoreView.setOnLightSeekChangeListener(new ShowMoreView.OnLightSeekChangeListener() {
                @Override
                public void onStart(SeekBar seekBar) {

                }

                @Override
                public void onProgress(SeekBar seekBar, int progress, boolean fromUser) {
                    setWidowBright(progress);
                    if(playerView != null){
                        playerView.setScreenBrightness(progress);
                    }
                }

                @Override
                public void onStop(SeekBar seekBar) {

                }
            });
            /**
             *
             * 初始化音量
             */
            if(playerView != null){
                int pro = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                showMoreView.setVoiceVolume(pro/15.0f);
            }
            showMoreView.setOnVoiceSeekChangeListener(new ShowMoreView.OnVoiceSeekChangeListener() {
                @Override
                public void onStart(SeekBar seekBar) {

                }

                @Override
                public void onProgress(SeekBar seekBar, int progress, boolean fromUser) {
                    AppLog.e("更多 ：sskbar"+progress);
                   if(playerView != null){
                       seekBar.setProgress(progress);
                       audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) ((progress * 15)/100.0),AudioManager.FLAG_PLAY_SOUND);
                   }
                }

                @Override
                public void onStop(SeekBar seekBar) {

                }
            });


            /**
             * 初始化倍速播放
             */
            showMoreView.setOnSpeedCheckedChangedListener(new ShowMoreView.OnSpeedCheckedChangedListener() {
                @Override
                public void onSpeedChanged(RadioGroup group, int checkedId) {
                    // 点击速度切换
                    if (checkedId == R.id.rb_speed_normal) {
                        playerView.changeSpeed(SpeedValue.One);
                    } else if (checkedId == R.id.rb_speed_onequartern) {
                        playerView.changeSpeed(SpeedValue.OneQuartern);
                    } else if (checkedId == R.id.rb_speed_onehalf) {
                        playerView.changeSpeed(SpeedValue.OneHalf);
                    } else if (checkedId == R.id.rb_speed_twice) {
                        playerView.changeSpeed(SpeedValue.Twice);
                    }
                }
            });
            /**
             * 视频填充模式
             */
            showMoreView.setAliPlayScalModeListener(new ShowMoreView.OnAliVodPlayScalModel() {

                @Override
                public void onVideoScalModel(IPlayer.ScaleMode mode) {
                    playerView.setScaleModel(mode);
                    switch (mode.getValue()){
                        case 0:
                            FixedToastUtils.show(VideoPlayAliActivity.this,"自适应模式");
                            break;
                        case 1:
                            FixedToastUtils.show(VideoPlayAliActivity.this,"填充模式");
                            break;
                        case 2:
                            FixedToastUtils.show(VideoPlayAliActivity.this,"拉伸模式");
                            break;
                    }
                    showMoreDialog.dismiss();
                }
            });

        }
    }


    //当前音量控制

    private class MyOnVolumListener implements AliyunVodPlayerView.OnVolumUpdateListener{

        @Override
        public void onVolumNum(float volum) {
            if(playerView != null){
                playerView.setCurrentVolume(volum);
            }
            /**
             *volum  100.0
             int streamType,  音量类型   播放器 或者 系统音量
             int index, 音量大小
             int flags
             flags参数：（下面是常用的几个）
             FLAG_PLAY_SOUND 调整音量时播放声音
             FLAG_SHOW_UI 调整时显示系统的音量进度条
             0 表示什么都不做
             */
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) ((volum * 15)/100.0),AudioManager.FLAG_PLAY_SOUND);
        }
    }

    /**   一些监听接口  结束*/


    @Override
    protected void onPause() {
        super.onPause();
        if(playerView != null){
            playerView.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (playerView != null) {
            playerView.setAutoPlay(false);
            playerView.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        if(playerView != null){
            playerView.onDestroy();
            playerView = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (playerView != null) {
            boolean handler = playerView.onKeyDown(keyCode, event);
            if (!handler) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(playerView != null){
            playerView.onResume();
        }
    }
}
