package zd.cn.novipvideo.view;

import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import zd.cn.novipvideo.R;

/**
 * Created by Administrator on 2019/7/17 0017.
 */

public class X5WebView extends WebView{
    public X5WebView(Context context) {
        super(context);
       // setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    public X5WebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setWebViewClient(client);
        initWebSetting();
        this.getView().setClickable(true);
    }

    private void initWebSetting() {
        WebSettings webSetting = this.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSetting.setTextZoom(100);
    }

    private  WebViewClient client = new WebViewClient(){
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            webView.loadUrl(url);
            return true;
        }
    };
}
