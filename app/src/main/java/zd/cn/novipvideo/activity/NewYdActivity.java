package zd.cn.novipvideo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import butterknife.Bind;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.ui.StatusBarActivity;
import zd.cn.novipvideo.utils.ScreenUtil;
import zd.cn.novipvideo.view.ImageTitleBar;

/**
 * 新手引导
 */
public class NewYdActivity extends StatusBarActivity{

    @Bind(R.id.new_yd)
    ImageTitleBar titleBar;
    @Bind(R.id.new_yd_img)
    ImageView newYd;
    @Bind(R.id.new_yd_img2)
    ImageView newYd2;
    @Bind(R.id.new_yd_img3)
    ImageView newYd3;
    @Bind(R.id.new_yd_img4)
    ImageView newYd4;
    @Bind(R.id.new_yd_img1)
    ImageView newYd1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_yd);
    }

    @Override
    protected void initView() {
        titleBar.setTitle("新手引导");
        ScreenUtil.setRelativeLayoutParams(newYd,0,0,10,20,45,45);
        ScreenUtil.setRelativeLayoutParams(newYd1,0,0,10,20,45,45);
        ScreenUtil.setRelativeLayoutParams(newYd2,0,0,10,20,45,45);
        ScreenUtil.setRelativeLayoutParams(newYd3,0,0,10,20,45,45);
        ScreenUtil.setRelativeLayoutParams(newYd4,0,0,10,20,45,45);

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

        newYd.setImageResource(R.mipmap.yd1);
        newYd1.setImageResource(R.mipmap.yd2);
        newYd2.setImageResource(R.mipmap.yd31);
        newYd3.setImageResource(R.mipmap.yd4);
        newYd4.setImageResource(R.mipmap.yd5);
    }
}
