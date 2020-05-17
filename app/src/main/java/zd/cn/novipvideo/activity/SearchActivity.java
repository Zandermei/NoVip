package zd.cn.novipvideo.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
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
import zd.cn.novipvideo.adapter.SpinnerAdapter;
import zd.cn.novipvideo.ui.AnalyActivity;
import zd.cn.novipvideo.ui.BrowserActivity;
import zd.cn.novipvideo.ui.StatusBarActivity;
import zd.cn.novipvideo.utils.AppLog;
import zd.cn.novipvideo.utils.ScreenUtil;
import zd.cn.novipvideo.utils.StatusBarUtils;
import zd.cn.novipvideo.utils.ToastUtils;
import zd.cn.novipvideo.utils.Url;
import zd.cn.novipvideo.utils.UrlParams;
import zd.cn.novipvideo.view.X5WebView;

public class SearchActivity extends StatusBarActivity implements View.OnClickListener{
    @Bind(R.id.web_fragment)
    FrameLayout fragmentWeb;
    @Bind(R.id.video_js_layout)
    LinearLayout videoJsLayout;//解析布局
    @Bind(R.id.video_js_spinner)
    Spinner videoSpinner;//显示线路
    @Bind(R.id.video_js_click)
    TextView videoJsClick;//解析视频



    @Bind(R.id.sear_layout)
    LinearLayout searchLayout;
    @Bind(R.id.sear_ed)
    EditText searEd;//搜索内容
    @Bind(R.id.search_tv)
    TextView searchTv;//搜索按钮
    @Bind(R.id.search_refresh)
    ImageView searchResh;//刷新按钮
    @Bind(R.id.search_pro)
    ProgressBar progressBar;

    @Bind(R.id.search_close)
    ImageView searchClose;//关闭加载


    private X5WebView webView;
    public static final int MSG_INIT_UI = 1;//初始化UI
    private static final int LOAD_WEB = 10;//webview准备好了 加载
    private String videoTitle;
    private String endUrl;//当前页面加载的网址
    private Context mContext;
    private SpinnerAdapter adapter;
    private int selectSpinnerPos = 0;//选中的线路
    private String intentMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setLayout() {
        if(Build.VERSION.SDK_INT >= 11){//开启硬件加速
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        StatusBarUtils.setStatusBarStyle(this,getResources().getColor(R.color.toorBar));
        setContentView(R.layout.activity_search);
        mHandler.sendEmptyMessageDelayed(MSG_INIT_UI,10);
    }

    @Override
    protected void initView() {
        mContext = this;
        ScreenUtil.setRelativeLayoutParams(searchLayout,0,90,0,0,0,0);
        ScreenUtil.setLinearLayoutParams(searEd,0,60,0,0,45,10);
        ScreenUtil.setTextSize(searEd,26);
        //搜索按钮
        ScreenUtil.setLinearLayoutParams(searchTv,100,60,0,0,15,25);
        ScreenUtil.setTextSize(searchTv,28);
        //刷新
        ScreenUtil.setLinearLayoutParams(searchResh,35,60,0,0,15,10);
        ScreenUtil.setLinearLayoutParams(searchClose,35,60,0,0,15,10);//停止加载按钮
        ScreenUtil.setRelativeLayoutParams(videoJsLayout,0,75,0,0,0,0);
        ScreenUtil.setLinearLayoutParams(videoSpinner,0,65,0,0,0,0);
        ScreenUtil.setLinearLayoutParams(videoJsClick,0,65,0,0,0,0);
        ScreenUtil.setTextSize(videoJsClick,30);
        videoJsClick.setOnClickListener(this);
        adapter = new SpinnerAdapter(this);//线路选择
        videoSpinner.setAdapter(adapter);

        searchResh.setOnClickListener(this);
        searchTv.setOnClickListener(this);
        searchClose.setOnClickListener(this);
    }


    @Override
    protected void setUpView() {
        intentMsg = getIntent().getStringExtra("msg");
        videoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectSpinnerPos = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    searEd.setText(endUrl);
                }
            }
        });


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



    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_INIT_UI:
                    initUI();
                    break;
                case LOAD_WEB:
                    if(!TextUtils.isEmpty(intentMsg)){
                        webView.loadUrl(Url.searchUrl+intentMsg);
                    }
                    break;

            }
            super.handleMessage(msg);
        }
    };

    private void initUI() {
        //创建webview添加到组件上
        webView = new X5WebView(mContext,null);
        fragmentWeb.addView(webView, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if(url.contains("http:") || url.contains("https:")){
                    webView.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                super.onPageFinished(webView, url);
                if(url.contains("html")){
                    videoJsLayout.setVisibility(View.VISIBLE);
                }else {
                    videoJsLayout.setVisibility(View.GONE);
                }
                endUrl = webView.getUrl();
                videoTitle = webView.getTitle();
                if(!TextUtils.isEmpty(videoTitle)){
                    searEd.setText(videoTitle);
                }
                //隐藏停止按钮 显示刷新按钮
                searchClose.setVisibility(View.GONE);
                searchResh.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                if(progressBar.getVisibility() == View.GONE){
                    progressBar.setVisibility(View.VISIBLE);
                }
                searEd.setText(webView.getUrl());
                searchClose.setVisibility(View.VISIBLE);
                searchResh.setVisibility(View.GONE);
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
                FrameLayout normalView = findViewById(R.id.web_view_xweb);
                //获取view的父节点
                ViewGroup viewGroup = webView;
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
        webSetting.setAppCachePath(mContext.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(mContext.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(mContext.getDir("geolocation", 0)
                .getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        CookieSyncManager.createInstance(mContext);
        CookieSyncManager.getInstance().sync();
        mHandler.sendEmptyMessage(10);
    }

    /**
     * 设置屏幕方向
     */
    private void fullScreen() {
        //如果是竖屏，强制横屏
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            searchLayout.setVisibility(View.GONE);
            AppLog.e("横屏");
        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            searchLayout.setVisibility(View.VISIBLE);
            AppLog.e("竖屏");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_close:
                searchClose.setVisibility(View.GONE);
                searchResh.setVisibility(View.VISIBLE);
                break;
            case R.id.search_tv:
                String input = searEd.getText().toString().trim();
                if(!TextUtils.isEmpty(input)){
                    if(input.contains("https:") || input.contains("http:")){
                        webView.loadUrl(input);
                    }else{
                        webView.loadUrl(Url.searchUrl+input);
                    }

                }
                break;
            case R.id.search_refresh:
                webView.reload();//刷新webview
                break;
            case R.id.video_js_click:
                if(selectSpinnerPos == 0){
                    //  startActivity(new Intent(this, VideoListActivity.class).putExtra("url", mIntentUrl).putExtra("title",videoTitle));
                    startActivity(new Intent(this, AnalyActivity.class).putExtra("url", endUrl).putExtra("title",videoTitle));
                }else {
                    startActivity(new Intent(this, BrowserActivity.class).putExtra("pos", selectSpinnerPos).putExtra("url", endUrl).putExtra("title",videoTitle));
                }
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
}
