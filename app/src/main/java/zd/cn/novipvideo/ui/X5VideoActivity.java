package zd.cn.novipvideo.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.tencent.smtt.utils.TbsLog;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.Bind;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.adapter.SpinnerAdapter;
import zd.cn.novipvideo.utils.AppLog;
import zd.cn.novipvideo.utils.ScreenUtil;
import zd.cn.novipvideo.utils.StatusBarUtils;
import zd.cn.novipvideo.utils.Url;
import zd.cn.novipvideo.view.ImageTitleBar;
import zd.cn.novipvideo.view.X5WebView;

/**
 * Created by Administrator on 2019/7/17 0017.
 */

public class X5VideoActivity extends StatusBarActivity implements View.OnClickListener{
    @Bind(R.id.fragment_xweb)
    FrameLayout fragmentWeb;
    @Bind(R.id.video_progress)
    ProgressBar progressBar;
    @Bind(R.id.title_bar_x)
    ImageTitleBar titleBar;//标题栏

    @Bind(R.id.video_js_layout)
    LinearLayout videoJsLayout;//解析布局
    @Bind(R.id.video_js_spinner)
    Spinner videoSpinner;//显示线路
    @Bind(R.id.video_js_click)
    TextView videoJsClick;//解析视频

    private SpinnerAdapter adapter;
    private String mIntentUrl = null;
    private int selectSpinnerPos = 0;//选中的线路

    private X5WebView webView;
    private int postion = 0;//主页面传来的地址位置
    public static final int MSG_INIT_UI = 1;//初始化UI
    private String videoTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setLayout() {
        //允许当前窗口透明
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        StatusBarUtils.setStatusBarStyle(this,getResources().getColor(R.color.toorBar));
        postion = getIntent().getIntExtra("url",0);
        if(Build.VERSION.SDK_INT >= 11){//开启硬件加速
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        setContentView(R.layout.activity_xweb);
        //发送一个消息初始化UI
        mHandler.sendEmptyMessageDelayed(MSG_INIT_UI,10);
    }

    @Override
    protected void initView() {
        titleBar.setLeftImgVisibility(View.VISIBLE);
        titleBar.setLeftListener(this);
        titleBar.setRightListener(this);
        titleBar.setRefshListener(this);
        ScreenUtil.setRelativeLayoutParams(videoJsLayout,0,75,0,0,0,0);
        ScreenUtil.setLinearLayoutParams(videoSpinner,0,65,0,0,0,0);
        ScreenUtil.setLinearLayoutParams(videoJsClick,0,65,0,0,0,0);
        ScreenUtil.setTextSize(videoJsClick,30);
        videoJsClick.setOnClickListener(this);
        adapter = new SpinnerAdapter(this);//线路选择
        videoSpinner.setAdapter(adapter);
    }
    @Override
    protected void setUpView() {
        videoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectSpinnerPos = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_INIT_UI:
                    initUI();
                    break;

            }
            super.handleMessage(msg);
        }
    };

    private void initUI() {
        //创建webview添加到组件上
        webView = new X5WebView(this,null);
        fragmentWeb.addView(webView,WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if(url.contains("http:") || url.contains("https:")){
                    webView.loadUrl(url);
                }
                return true;
                //  www.taobao.com/123   www.taobao.com/1234
                //这里我们需要加载 123，返回true表示自己处理加载的网址，1234网址不再加载
                //返回false则由webview处理加载，加载完123之后，webview还会加载1234，和我们想要的不一样 所以返回true
            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                super.onPageFinished(webView, url);
                if(url.contains("http")){
                    videoJsLayout.setVisibility(View.VISIBLE);
                }else {
                    videoJsLayout.setVisibility(View.GONE);
                }
                mIntentUrl = url;
                videoTitle = webView.getTitle();
                if(!TextUtils.isEmpty(videoTitle)){
                    titleBar.setTitle(videoTitle);
                }
                titleBar.setRightImgVisibility(View.GONE);
                titleBar.setRefshImgVisibility(View.VISIBLE);
            }
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                if(progressBar.getVisibility() == View.GONE){
                    progressBar.setVisibility(View.VISIBLE);
                }
                titleBar.setRightImgVisibility(View.VISIBLE);
                titleBar.setRefshImgVisibility(View.GONE);
            }
            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();//忽略证书
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            View myVideoView;//播放视频view
            View myNormalView;//正常显示的view
            IX5WebChromeClient.CustomViewCallback callback;

            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                super.onProgressChanged(webView, newProgress);
                if(progressBar != null){
                    if(newProgress > 0 && newProgress <100){
                        progressBar.setProgress(newProgress);
                    }else {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback
                    customViewCallback) {
                fullScreen();
                FrameLayout normalView = webView;
                ViewGroup viewGroup = (ViewGroup) normalView.getParent();
                viewGroup.removeView(normalView);
                viewGroup.addView(view);
                myVideoView = view;
                myVideoView = normalView;
                callback = customViewCallback;

            }

            @Override
            public void onHideCustomView() {
                fullScreen();
                if(callback != null){
                    callback.onCustomViewHidden();
                    callback = null;
                }
                if(myVideoView != null){
                    ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                    viewGroup.removeView(myVideoView);
                    viewGroup.addView(myNormalView);
                }
            }
        });

        WebSettings webSetting = webView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webView.loadUrl(Url.videoUrl[postion]);
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation){
            case Configuration.ORIENTATION_LANDSCAPE://如果横屏，去掉标题栏，显示全屏
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;
            case Configuration.ORIENTATION_PORTRAIT://如果竖屏 去掉全屏，添加标题栏
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                break;
        }
    }

    /**
     * 设置屏幕方向
     */
    private void fullScreen() {
        //如果是竖屏，强制横屏
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            titleBar.setVisibility(View.GONE);
            AppLog.e("横屏");
        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            titleBar.setVisibility(View.VISIBLE);
            AppLog.e("竖屏");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.video_js_click:
                if(selectSpinnerPos == 0){
                  //  startActivity(new Intent(this, VideoListActivity.class).putExtra("url", mIntentUrl).putExtra("title",videoTitle));
                    startActivity(new Intent(this, BrowserOneActivity.class).putExtra("pos", selectSpinnerPos).putExtra("url", mIntentUrl).putExtra("title",videoTitle));
                }else {
                    startActivity(new Intent(this, BrowserActivity.class).putExtra("pos", selectSpinnerPos).putExtra("url", mIntentUrl).putExtra("title",videoTitle));
                }
                break;
            case R.id.title_bar_img:
                if(webView.canGoBack()){
                    webView.goBack();
                }else {
                    finish();
                }
                break;
            case R.id.title_right://停止加载，刷新
                titleBar.setRightImgVisibility(View.GONE);
                titleBar.setRefshImgVisibility(View.VISIBLE);
                webView.stopLoading();
                break;
            case R.id.title_refsh://刷新
                webView.reload();
                titleBar.setRefshImgVisibility(View.GONE);
                titleBar.setRightImgVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView != null && webView.canGoBack()) {
                webView.goBack();
                return true;
            } else{
                finish();
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(webView != null){
            webView.destroy();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(webView != null){
            webView.stopLoading();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(webView != null){
            webView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(webView != null){
            webView.onResume();
        }
    }
}
