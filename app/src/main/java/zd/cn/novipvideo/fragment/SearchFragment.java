package zd.cn.novipvideo.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.Bind;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.activity.MainActivity;
import zd.cn.novipvideo.activity.SearchActivity;
import zd.cn.novipvideo.ui.SearchWebView;
import zd.cn.novipvideo.utils.AppLog;
import zd.cn.novipvideo.utils.ScreenUtil;
import zd.cn.novipvideo.utils.ToastUtils;
import zd.cn.novipvideo.utils.Url;
import zd.cn.novipvideo.view.ImageTitleBar;
import zd.cn.novipvideo.view.X5WebView;

public class SearchFragment extends BaseFragment implements View.OnClickListener,FragmentOnKeyListener{

    @Bind(R.id.fragment_title)
    ImageTitleBar titleBar;
    @Bind(R.id.fragment_tv)
    TextView fragmentTv;//首页
    @Bind(R.id.fragment_layout)
    RelativeLayout fragmentLayout;//搜索框
    @Bind(R.id.fragment_ed)
    EditText fragmentEd;//搜索输入
    @Bind(R.id.fragment_img)
    ImageView fragmentImg;//搜索按钮
    //小图标 布局
    @Bind(R.id.searweb_layout)
    RelativeLayout searchLayout;
    @Bind(R.id.search_net)
    TextView searNet;//网址
    @Bind(R.id.search_zx)
    TextView searZx;//资讯
    @Bind(R.id.search_bd)
    TextView searchBd;//百度
    @Bind(R.id.search_video)
    TextView searchVideo;//视频


    @Override
    protected int setLayout() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView() {
        titleBar.setTitle("新闻");
        mContext = getContext();
        ScreenUtil.setLinearLayoutParams(fragmentTv,0,90,160,0,55,55);
        ScreenUtil.setTextSize(fragmentTv,45);
        ScreenUtil.setLinearLayoutParams(fragmentLayout,0,80,45,0,45,45);
        ScreenUtil.setRelativeLayoutParams(fragmentEd,0,60,0,0,0,65);
        ScreenUtil.setTextSize(fragmentEd,32);
        ScreenUtil.setRelativeLayoutParams(fragmentImg,35,60,0,0,0,20);
        //小图标 布局
        ScreenUtil.setLinearLayoutParams(searchLayout,0,160,50,0,45,45);
        ScreenUtil.setRelativeLayoutParams(searNet,120,120,0,0,0,20);
        ScreenUtil.setRelativeLayoutParams(searZx,120,120,0,0,0,20);
        ScreenUtil.setRelativeLayoutParams(searchBd,120,120,0,0,0,20);
        ScreenUtil.setRelativeLayoutParams(searchVideo,120,120,0,0,0,20);
        ScreenUtil.setTextSize(searNet,26);
        ScreenUtil.setTextSize(searZx,26);
        ScreenUtil.setTextSize(searchBd,26);
        ScreenUtil.setTextSize(searchVideo,26);
        //点击事件
        fragmentImg.setOnClickListener(this);
        searNet.setOnClickListener(this);
        searchBd.setOnClickListener(this);
        searZx.setOnClickListener(this);
        searchVideo.setOnClickListener(this);
    }

    @Override
    protected void setUpView() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_img:
                String input = fragmentEd.getText().toString().trim();
                if(!TextUtils.isEmpty(input)){
                    startActivity(new Intent(getContext(), SearchActivity.class).putExtra("msg",input));
                }
                break;
            case R.id.search_net://网址
                startActivity(new Intent(mContext, SearchWebView.class).putExtra("position",0));
                break;
            case R.id.search_zx://资讯
                startActivity(new Intent(mContext, SearchWebView.class).putExtra("position",1));
                break;
            case R.id.search_bd://百度
                startActivity(new Intent(mContext, SearchWebView.class).putExtra("position",2));
                break;
            case R.id.search_video://视频
                startActivity(new Intent(mContext, SearchWebView.class).putExtra("position",3));
                break;


        }
    }

    /**
     *自己定义的按键回调方法
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }


}
