package zd.cn.novipvideo.ui;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.IOException;

import butterknife.Bind;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.utils.AppLog;
import zd.cn.novipvideo.utils.CommentUtl;

public class VideoPlaysActivity extends StatusBarActivity implements View.OnClickListener{
    @Bind(R.id.video_plays)
    VideoView videoPlayView;//播放器view
    @Bind(R.id.seekbar_progress)
    SeekBar seekBar;//播放器进度条
    @Bind(R.id.play_pause_btn)
    ImageButton playAndPauseBtn;//暂停
    @Bind(R.id.play_tv_current)
    TextView currentTime;//当前时间
    @Bind(R.id.play_tv_totle)
    TextView totleTime;//时间总成
    @Bind(R.id.full_screen)
    ImageView fullScreen;//全屏按钮
    @Bind(R.id.video_progress)
    ProgressWheel progreLoading;//加载进度条

    private boolean fullscreen = false;//是否全屏标记


    private MediaPlayer mediaPlayer = new MediaPlayer();//播放器重要组件
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_play_video);
    }

    @Override
    protected void initView() {
        playAndPauseBtn.setOnClickListener(this);
        fullScreen.setOnClickListener(this);

        videoPlayView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progreLoading.setVisibility(View.GONE);
                totleTime.setText(CommentUtl.timeToString(mp.getDuration()));
                AppLog.e("视频准备就绪:"+ CommentUtl.timeToString(mp.getDuration()));
                videoPlayView.start();


                mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {

                        int currentPos = mp.getCurrentPosition();
                        int duration = mp.getDuration();
                        int pro = ((currentPos * 100)/duration);
                        seekBar.setProgress(30);
                        seekBar.setSecondaryProgress(50);//缓存进度条
                        if(currentPos == percent){
                            progreLoading.setVisibility(View.VISIBLE);
                        }else {
                           progreLoading.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        videoPlayView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                AppLog.e("视频播放结束");
                AppLog.e("视频准备就绪:"+ CommentUtl.timeToString(mp.getCurrentPosition()));
            }
        });


        videoPlayView.setVideoURI(Uri.parse("https://letv.com-t-letv.com/20190709/3240_c18e4bcc/index.m3u8"));
    }

    @Override
    protected void setUpView() {
        super.setUpView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_pause_btn://暂停或播放
                if(videoPlayView.isPlaying()){
                    videoPlayView.pause();
                    playAndPauseBtn.setImageResource(R.mipmap.ic_vod_play_normal);
                }else {
                    videoPlayView.start();
                    playAndPauseBtn.setImageResource(R.mipmap.ic_vod_pause_normal);
                }
                break;
            case R.id.full_screen://全屏
                if(!fullscreen){//设置RelativeLayout的全屏模式
                    RelativeLayout.LayoutParams layoutParams=
                            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    videoPlayView.setLayoutParams(layoutParams);

                    fullscreen = true;//改变全屏/窗口的标记
                }else{//设置RelativeLayout的窗口模式
                    RelativeLayout.LayoutParams lp=new  RelativeLayout.LayoutParams(320,240);
                    lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                    videoPlayView.setLayoutParams(lp);
                    fullscreen = false;//改变全屏/窗口的标记
                }
                break;
        }
    }
}
