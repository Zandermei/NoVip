//package zd.cn.novipvideo.ui;
//
//import android.annotation.SuppressLint;
//import android.graphics.Bitmap;
//import android.graphics.PixelFormat;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.View;
//import android.webkit.WebResourceError;
//import android.webkit.WebResourceRequest;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
//import butterknife.Bind;
//import zd.cn.novipvideo.R;
//import zd.cn.novipvideo.utils.AppLog;
//import zd.cn.novipvideo.utils.ScreenUtil;
//import zd.cn.novipvideo.view.ImageTitleBar;
//
//public class ActivityContent extends StatusBarActivity {
//    @Bind(R.id.title_bar)
//    ImageTitleBar titleBar;
//    @Bind(R.id.web_view)
//    WebView webView;
//
//
//
//    private String title, url;
//
//    @Override
//    protected void onCreate( Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().setFormat(PixelFormat.TRANSLUCENT);//（这个对宿主没什么影响，建议声明）
//    }
//
//    @Override
//    protected void setLayout() {
//        setContentView(R.layout.activity_content);
//        title = getIntent().getStringExtra("title");
//        url = getIntent().getStringExtra("url");
//    }
//
//    @Override
//    protected void initView() {
//        binding.titleBar.setImageTitle(title);
//        binding.titleBar.setLeftImgVisibily(View.VISIBLE);
//        ScreenUtil.setLinearLayoutParams(binding.netImg,300,280,0,15,0,0);
//        ScreenUtil.setLinearLayoutParams(binding.netReplay,200,70,0,0,0,0);
//        ScreenUtil.setTextSize(binding.netReplay,30);
//        binding.titleBar.setLeftClick(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (binding.webView.canGoBack()) {
//                    binding.webView.goBack();
//                } else {
//                    finish();
//                }
//            }
//        });
//
//        binding.netReplay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.webView.loadUrl(url);
//            }
//        });
//
//        if(!NetUtils.hasNetwork(this)){
//            binding.netLayout.setVisibility(View.VISIBLE);
//        }else {
//            binding.netLayout.setVisibility(View.GONE);
//        }
//
//    }
//
//    @Override
//    protected void setUpView() {
//        WebSettings settings = binding.webView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
//        settings.setPluginState(WebSettings.PluginState.ON);
//
//        final String javaSript = "javascript:function hinde(){ " +
//                "for (var index = 4;index < 6;index++) {" +
//                "document.getElementsByClassName('ui-btn')[index].style.visibility='hidden'"
//                + "}}";
//
//        final String javaSc2 = "javascript:function hinde2(){" +
//                "document.getElementsByClassName('footer')[0].style.display = 'none'" +
//                "}";
//        binding.webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
//                if (url.contains("http:") || url.contains("https:")) {
//                    webView.loadUrl(url);
//                }
//                return true;
//            }
//
//            @Override
//            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
//                super.onPageStarted(webView, s, bitmap);
//                binding.progress.setVisibility(View.VISIBLE);
//                binding.netLayout.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onPageFinished(WebView webView, String s) {
//                super.onPageFinished(webView, s);
//                webView.loadUrl(javaSript);
//                webView.loadUrl(javaSc2);
//                webView.loadUrl("javascript:hinde();");
//                webView.loadUrl("javascript:hinde2();");
//                binding.progress.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//                binding.netLayout.setVisibility(View.VISIBLE);
//            }
//
//
//        });
//        binding.webView.loadUrl(url);
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        binding.webView.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        binding.webView.onResume();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        binding.webView.destroy();
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (binding.webView.canGoBack()) {
//                binding.webView.goBack();
//                return true;
//            }else {
//                finish();
//            }
//        }
//        return false;
//
//    }
//}
