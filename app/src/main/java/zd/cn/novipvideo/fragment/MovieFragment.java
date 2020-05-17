package zd.cn.novipvideo.fragment;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

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
import zd.cn.novipvideo.ui.BrowserActivity;
import zd.cn.novipvideo.utils.AppLog;
import zd.cn.novipvideo.utils.CommentUtl;
import zd.cn.novipvideo.utils.Url;
import zd.cn.novipvideo.view.ImageTitleBar;
import zd.cn.novipvideo.view.X5WebView;

/**
 * 电影
 */
public class MovieFragment extends BaseFragment{
    @Bind(R.id.movie_title)
    ImageTitleBar titleBar;
    @Bind(R.id.fragment_xweb)
    FrameLayout fragmentWeb;
    @Bind(R.id.video_progress)
    ProgressBar progressBar;

    private X5WebView webView;
    public static final int MSG_INIT_UI = 1;//初始化UI

    @Override
    protected int setLayout() {
        if(Build.VERSION.SDK_INT >= 11){//开启硬件加速
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        return R.layout.fragment_movie;
    }

    @Override
    protected void initView() {
        titleBar.setTitle("后退");
        titleBar.setLeftImgVisibility(View.VISIBLE);
        //发送一个消息初始化UI
        mHandler.sendEmptyMessageDelayed(MSG_INIT_UI,10);
        titleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(webView.canGoBack()){
                    webView.goBack();
                }else {
                    return;
                }
            }
        });
    }

    @Override
    protected void setUpView() {

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
        webView = new X5WebView(mContext,null);
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
                webView.loadUrl(CommentUtl.getClearAdDivJs(mContext));            }
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                if(progressBar.getVisibility() == View.GONE){
                    progressBar.setVisibility(View.VISIBLE);
                }
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
                webView.loadUrl(CommentUtl.getClearAdDivJs(mContext));
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
        webSetting.setAppCachePath(mContext.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(mContext.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(mContext.getDir("geolocation", 0)
                .getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webView.loadUrl(Url.sigUrl);
        CookieSyncManager.createInstance(mContext);
        CookieSyncManager.getInstance().sync();
    }

    private void fullScreen() {
        //如果是竖屏，强制横屏
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            titleBar.setVisibility(View.GONE);
            AppLog.e("横屏");
        }else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            titleBar.setVisibility(View.VISIBLE);
            AppLog.e("竖屏");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(webView != null){
            webView.destroy();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(webView != null){
            webView.stopLoading();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(webView != null){
            webView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(webView != null){
            webView.onResume();
        }
    }

}
