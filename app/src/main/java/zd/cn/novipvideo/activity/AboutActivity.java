package zd.cn.novipvideo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.ui.StatusBarActivity;
import zd.cn.novipvideo.utils.ScreenUtil;
import zd.cn.novipvideo.view.ImageTitleBar;

/**
 * 关于程序
 */
public class AboutActivity extends StatusBarActivity{
    @Bind(R.id.about_title)
    ImageTitleBar titleBar;
    @Bind(R.id.about_name)
    TextView aboutName;

    @Bind(R.id.about_content)
    TextView aboutContent;
    @Bind(R.id.about_mail)
    TextView aboutMail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_about);
    }

    @Override
    protected void initView() {
        titleBar.setTitle("关于程序");
        ScreenUtil.setRelativeLayoutParams(aboutName,0,100,45,0,0,0);
        ScreenUtil.setTextSize(aboutName,40);
        //内容
        ScreenUtil.setRelativeLayoutParams(aboutContent,0,0,30,0,45,45);
        ScreenUtil.setTextSize(aboutContent,28);
        ScreenUtil.setRelativeLayoutParams(aboutMail,0,60,30,0,45,45);
        ScreenUtil.setTextSize(aboutMail,26);

    }

    @Override
    protected void setUpView() {
        titleBar.setLeftImgVisibility(View.VISIBLE);
        titleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
