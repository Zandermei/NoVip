package zd.cn.novipvideo.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.Date;

import butterknife.Bind;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.activity.AboutActivity;
import zd.cn.novipvideo.activity.MainActivity;
import zd.cn.novipvideo.activity.NewYdActivity;
import zd.cn.novipvideo.ui.LoginActivity;
import zd.cn.novipvideo.utils.AppLog;
import zd.cn.novipvideo.utils.DataManagerUtils;
import zd.cn.novipvideo.utils.ScreenUtil;
import zd.cn.novipvideo.utils.ToastUtils;
import zd.cn.novipvideo.utils.Url;
import zd.cn.novipvideo.view.ImageTitleBar;
import zd.cn.novipvideo.view.X5WebView;

public class AccountFragment extends BaseFragment implements View.OnClickListener{

    @Bind(R.id.title_bar_x)
    ImageTitleBar titleBar;//标题栏
    @Bind(R.id.account_bg)
    RelativeLayout accountBg;//账号背景
    @Bind(R.id.account_img)
    ImageView accountImg;//账号头像
    @Bind(R.id.account_tv)
    TextView accountNick;//昵称

    @Bind(R.id.account_line_layout)
    RelativeLayout lintLayout;//菜单
    @Bind(R.id.account_line_tv)
    TextView lineTv;//引导
    @Bind(R.id.account_line_img)
    ImageView lineImg;

    @Bind(R.id.account_line1_layout)
    RelativeLayout lint1Layout;//菜单
    @Bind(R.id.account_line1_tv)
    TextView line1Tv;//引导
    @Bind(R.id.account_line1_img)
    ImageView line1Img;

    @Bind(R.id.account_line2_layout)
    RelativeLayout lint2Layout;//菜单
    @Bind(R.id.account_line2_tv)
    TextView line2Tv;//引导
    @Bind(R.id.account_line2_img)
    ImageView line2Img;
    @Bind(R.id.login_out)
    Button loginOut;//退出



    @Override
    protected int setLayout() {
        return R.layout.fragment_account;
    }

    @Override
    protected void initView() {
        titleBar.setTitle("账户");
        ScreenUtil.setLinearLayoutParams(accountBg,0,255,0,0,0,0);
        ScreenUtil.setRelativeLayoutParams(accountImg,130,130,0,0,30,20);
        ScreenUtil.setRelativeLayoutParams(accountNick,0,60,0,0,0,0);
        ScreenUtil.setTextSize(accountNick,32);
        //菜单1
        ScreenUtil.setLinearLayoutParams(lintLayout,0,90,0,0,0,0);
        ScreenUtil.setRelativeLayoutParams(lineTv,0,60,0,0,30,0);
        ScreenUtil.setRelativeLayoutParams(lineImg,30,35,0,0,0,30);
        ScreenUtil.setTextSize(lineTv,28);
        //菜单2
        ScreenUtil.setLinearLayoutParams(lint1Layout,0,90,0,0,0,0);
        ScreenUtil.setRelativeLayoutParams(line1Tv,0,60,0,0,30,0);
        ScreenUtil.setRelativeLayoutParams(line1Img,30,35,0,0,0,30);
        ScreenUtil.setTextSize(line1Tv,28);

        //菜单3
        ScreenUtil.setLinearLayoutParams(lint2Layout,0,90,15,0,0,0);
        ScreenUtil.setRelativeLayoutParams(line2Tv,0,60,0,0,30,0);
        ScreenUtil.setRelativeLayoutParams(line2Img,30,35,0,0,0,30);
        ScreenUtil.setTextSize(line2Tv,28);
        //退出
        ScreenUtil.setLinearLayoutParams(loginOut,0,90,220,50,45,45);
        ScreenUtil.setTextSize(loginOut,32);

    }

    @Override
    protected void setUpView() {
        accountNick.setText(DataManagerUtils.getCurentUser());
        lintLayout.setOnClickListener(this);
        lint1Layout.setOnClickListener(this);
        lint2Layout.setOnClickListener(this);
        loginOut.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.account_line_layout://新手引导
                startActivity(new Intent(mContext,NewYdActivity.class));
                break;
            case R.id.account_line1_layout://关于程序
                startActivity(new Intent(mContext, AboutActivity.class));
                break;
            case R.id.account_line2_layout://分享
                ToastUtils.showCenter(mContext,"暂不能使用");
                break;
            case R.id.login_out:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;

        }
    }

}
