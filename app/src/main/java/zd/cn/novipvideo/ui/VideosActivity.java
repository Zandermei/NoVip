package zd.cn.novipvideo.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;


import butterknife.Bind;
import butterknife.ButterKnife;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.adapter.SpinnerAdapter;
import zd.cn.novipvideo.utils.AppLog;
import zd.cn.novipvideo.utils.ScreenUtil;
import zd.cn.novipvideo.utils.StatusBarUtils;

/**
 * Created by Administrator on 2019/7/16 0016.
 */

public class VideosActivity extends StatusBarActivity implements View.OnClickListener{
    @Bind(R.id.web_filechooser)
    WebView webView;
    @Bind(R.id.web_fragment)
    FrameLayout webFragment;
    @Bind(R.id.progressBar1)
    ProgressBar progressBar;

    @Bind(R.id.video_js_layout)
    LinearLayout videoJsLayout;//解析布局
    @Bind(R.id.video_js_spinner)
    Spinner videoSpinner;//显示线路
    @Bind(R.id.video_js_click)
    TextView videoJsClick;//解析视频

    private SpinnerAdapter adapter;
    private String mIntentUrl = null;
    private int selectSpinnerPos = 0;//选中的线路
    private String[] videoUrl = {"https://3g.v.qq.com/", "https://youku.com/", "https://m.iqiyi.com/",
            "http://m.le.com/", "https://m.mgtv.com/channel/home/", "https://m.tv.sohu.com/"};
    private int postion;//网址标记

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    protected void setUpView() {

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                if(s.startsWith("http:") || s.startsWith("https:")){
                    if(!s.contains("player")){
                        webView.loadUrl(s);
                    }
                }
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(progressBar.getVisibility() == View.GONE){
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
             //   if(view.isActivated()){
                    if(url.contains("html")){
                        videoJsLayout.setVisibility(View.VISIBLE);
                    }else {
                        videoJsLayout.setVisibility(View.GONE);
                    }
              //  }
                AppLog.e("加载结22束："+url+"----"+videoJsLayout.getVisibility());
                mIntentUrl = url;

            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                //super.onReceivedSslError(webView, sslErrorHandler, sslError);
                sslErrorHandler.proceed();//忽略ssl证书
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            CustomViewCallback callback;
            //横屏时回调
            @Override
            public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
                fullScreen();
                webView.setVisibility(View.GONE);
                webFragment.setVisibility(View.VISIBLE);
                webFragment.addView(view);
                callback = customViewCallback;
                super.onShowCustomView(view, customViewCallback);
            }

            //竖屏时回调
            @Override
            public void onHideCustomView() {
                fullScreen();
                //竖屏时显示webview 隐藏 webfragment 并移除横屏添加的view
                webView.setVisibility(View.VISIBLE);
                webFragment.setVisibility(View.GONE);
                webFragment.removeAllViews();
                super.onHideCustomView();
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(progressBar != null){
                    if(newProgress > 0 && newProgress <100){
                        progressBar.setProgress(newProgress);
                    }else {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

        /**
         * 线路选择监听
         */
        videoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectSpinnerPos = position;
                AppLog.e("选择线路："+position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 设置屏幕方向
     */
    private void fullScreen() {
        //如果是竖屏，强制横屏
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            AppLog.e("横屏");
        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            AppLog.e("竖屏");
        }
    }

    @Override
    protected void initView() {
        ScreenUtil.setRelativeLayoutParams(videoJsLayout,0,75,0,0,0,0);
        ScreenUtil.setLinearLayoutParams(videoSpinner,0,65,0,0,0,0);
        ScreenUtil.setLinearLayoutParams(videoJsClick,0,65,0,0,0,0);
        ScreenUtil.setTextSize(videoJsClick,30);
        videoJsClick.setOnClickListener(this);
        postion = getIntent().getIntExtra("url",0);
        initSetting();
        webView.loadUrl(videoUrl[postion]);
        adapter = new SpinnerAdapter(this);//线路选择
        videoSpinner.setAdapter(adapter);
    }

    @Override
    protected void setLayout() {
        StatusBarUtils.setStatusBarStyle(this,getResources().getColor(R.color.titleBar));
        setContentView(R.layout.activity_videos);
    }

    private void initSetting() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);                    //支持Javascript 与js交互
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        settings.setAllowFileAccess(true);                      //设置可以访问文件
        settings.setSupportZoom(true);                          //支持缩放
        settings.setBuiltInZoomControls(true);                  //设置内置的缩放控件
        settings.setUseWideViewPort(true);                      //自适应屏幕
        settings.setSupportMultipleWindows(true);               //多窗口
        settings.setDefaultTextEncodingName("utf-8");            //设置编码格式
        settings.setAppCacheEnabled(true);                      //设置APP是否可以缓存
        settings.setDomStorageEnabled(true);//开启dom存储
        settings.setAppCacheMaxSize(Long.MAX_VALUE);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);       //缓存模式
        settings.setTextZoom(100);//设置字体大小，不使用系统文字，否则布局错乱

    }


    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        webView.stopLoading();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.video_js_click:
               // startActivity(new Intent(this,VideoListActivity.class).putExtra("url",mIntentUrl));
                startActivity(new Intent(this,X5VideoActivity.class).putExtra("pos",selectSpinnerPos).putExtra("url",mIntentUrl));
                break;
        }
    }
}
