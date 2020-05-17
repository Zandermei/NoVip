package zd.cn.novipvideo.ui;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;


import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.CookieSyncManager;

import butterknife.Bind;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.utils.AppLog;
import zd.cn.novipvideo.utils.CommentUtl;
import zd.cn.novipvideo.utils.OkHttpUtils;
import zd.cn.novipvideo.utils.Url;

/**
 * Created by EDZ on 2019/7/30.
 */

public class TestVideoActivit extends StatusBarActivity{

    private X5WebView mWebView;
    private ViewGroup mViewParent;
    private ProgressBar progressBar;


    @Override
    protected void setUpView() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_test);
        mViewParent = (ViewGroup) findViewById(R.id.webView1);
        progressBar = findViewById(R.id.progressBar1);
        mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);
    }
    public static final int MSG_INIT_UI = 1;

    private Handler mTestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INIT_UI:
                    init();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void init() {
        mWebView = new X5WebView(this, null);
        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT));

        mWebView.setWebViewClient(new com.tencent.smtt.sdk.WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(com.tencent.smtt.sdk.WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                if(progressBar.getVisibility() == View.GONE){
                    progressBar.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onLoadResource(com.tencent.smtt.sdk.WebView webView, String s) {
                super.onLoadResource(webView, s);
            }
            @Override
            public void onPageFinished(com.tencent.smtt.sdk.WebView webView, String s) {
                super.onPageFinished(webView, s);
                //修改标题
                webView.loadUrl(CommentUtl.cre("http://api.sigujx.com/?url=https://v.qq.com/x/cover/mzc0020025uh2wp/m0032n1s3kr.html"));
            }
            @Override
            public void onReceivedSslError(com.tencent.smtt.sdk.WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();//忽略证书
            }
        });

        mWebView.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient() {
            View myVideoView;
            View myNormalView;
            IX5WebChromeClient.CustomViewCallback callback;
            @Override
            public void onProgressChanged(com.tencent.smtt.sdk.WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if(i > 0 && i <100){
                    progressBar.setProgress(i);
                }else {
                    progressBar.setVisibility(View.GONE);
                }
            }
            /**
             * 全屏播放配置
             */
            @Override
            public void onShowCustomView(View view,
                                         IX5WebChromeClient.CustomViewCallback customViewCallback) {
                fullScreen();
                FrameLayout normalView = mWebView;
                ViewGroup viewGroup = (ViewGroup) normalView.getParent();
                viewGroup.removeView(normalView);
                viewGroup.addView(view);
                myVideoView = view;
                myNormalView = normalView;
                callback = customViewCallback;
            }

            @Override
            public void onHideCustomView() {
                AppLog.e("onHideCustomView");
                fullScreen();
                if (callback != null) {
                    callback.onCustomViewHidden();
                    callback = null;
                }
                if (myVideoView != null) {
                    ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                    viewGroup.removeView(myVideoView);
                    viewGroup.addView(myNormalView);
                }
            }
        });

        com.tencent.smtt.sdk.WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        //加载iframe使用电脑版代理头
        webSetting.setUserAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");

        webSetting.setPluginState(com.tencent.smtt.sdk.WebSettings.PluginState.ON);
        //webSetting.setMixedContentMode(0);//允许http和https资源混合加载

        String userAgent = mWebView.getSettings().getUserAgentString();
        if (!TextUtils.isEmpty(userAgent)) {
            mWebView.getSettings().setUserAgentString(userAgent
                    .replace("Android", "")
                    .replace("android", "")
                    + " cldc");
        }
            mWebView.loadUrl("file:///android_asset/seven.html");

        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }

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



    @JavascriptInterface
    public void closeCrentWeb(){
        AppLog.e("网页点击返回");
        finish();
    }
}
