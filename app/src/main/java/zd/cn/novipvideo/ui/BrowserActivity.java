package zd.cn.novipvideo.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ZoomButtonsController;


import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.utils.AppLog;
import zd.cn.novipvideo.utils.CommentUtl;
import zd.cn.novipvideo.utils.OkHttpUtils;
import zd.cn.novipvideo.utils.StatusBarUtils;
import zd.cn.novipvideo.utils.ToastUtils;
import zd.cn.novipvideo.utils.Url;
import zd.cn.novipvideo.view.ImageTitleBar;

public class BrowserActivity extends StatusBarActivity {
	/**
	 * 作为一个浏览器的示例展示出来，采用android+web的模式
	 */
	private X5WebView mWebView;
	private ViewGroup mViewParent;
	private ProgressBar progressBar;

	private int position = 0;
	private String intentUrl;
	private String intentTitle = "畅看视频";
	private ValueCallback<Uri> uploadFile;

	@Bind(R.id.title_brow)
	ImageTitleBar titleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void setLayout() {
		StatusBarUtils.setStatusBarStyle(this,getResources().getColor(R.color.toorBar));
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		Intent intent = getIntent();
		position = intent.getIntExtra("pos",0);
		intentUrl = intent.getStringExtra("url");
		intentTitle = intent.getStringExtra("title");

		if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
			getWindow()
					.setFlags(
							android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
							android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		}
		setContentView(R.layout.activity_browser);
		mViewParent = (ViewGroup) findViewById(R.id.webView1);
		progressBar = findViewById(R.id.progressBar1);
		mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);
	}

	private void init() {
		mWebView = new X5WebView(this, null);
		mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.FILL_PARENT));

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}

			@Override
			public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
				super.onPageStarted(webView, s, bitmap);
				if(progressBar.getVisibility() == View.GONE){
					progressBar.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void onLoadResource(WebView webView, String s) {
				super.onLoadResource(webView, s);
			}

			@Override
			public void onPageFinished(final WebView webView, String s) {
				super.onPageFinished(webView, s);
				//修改标题
               runOnUiThread(new Runnable() {
				   @Override
				   public void run() {
					   webView.loadUrl(CommentUtl.setTitle(intentTitle));
					   webView.loadUrl(CommentUtl.getClearAdDivJs(BrowserActivity.this));
				   }
			   });
			}

			@Override
			public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
				sslErrorHandler.proceed();//忽略证书
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient() {
			View myVideoView;
			View myNormalView;
			CustomViewCallback callback;


			@Override
			public void onProgressChanged(WebView webView, int i) {
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
					CustomViewCallback customViewCallback) {
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

		WebSettings webSetting = mWebView.getSettings();
		webSetting.setAllowFileAccess(true);
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
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
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		//webSetting.setMixedContentMode(0);//允许http和https资源混合加载
		AppLog.e("webSetting.getMixedContentMode():"+webSetting.getMixedContentMode());

		String userAgent = mWebView.getSettings().getUserAgentString();
		if (!TextUtils.isEmpty(userAgent)) {
			mWebView.getSettings().setUserAgentString(userAgent
					.replace("Android", "")
					.replace("android", "")
					+ " cldc");
		}

		if(position == OkHttpUtils.INTENT_CODE){
			mWebView.loadUrl(intentUrl);
		}else {
			mWebView.loadUrl(Url.urlJson[position]+intentUrl);
		}


		CookieSyncManager.createInstance(this);
		CookieSyncManager.getInstance().sync();
	}


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
	protected void initView() {
		titleBar.setTitle("畅看视频");
		titleBar.setLeftImgVisibility(View.VISIBLE);
		titleBar.setLeftListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView != null && mWebView.canGoBack()) {
				mWebView.goBack();
				return true;
			} else
				return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 0:
				if (null != uploadFile) {
					Uri result = data == null || resultCode != RESULT_OK ? null
							: data.getData();
					uploadFile.onReceiveValue(result);
					uploadFile = null;
				}
				break;
			default:
				break;
			}
		} else if (resultCode == RESULT_CANCELED) {
			if (null != uploadFile) {
				uploadFile.onReceiveValue(null);
				uploadFile = null;
			}
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (intent == null || mWebView == null || intent.getData() == null)
			return;
		mWebView.loadUrl(intent.getData().toString());
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
	protected void onDestroy() {
		if (mTestHandler != null)
			mTestHandler.removeCallbacksAndMessages(null);
		if (mWebView != null)
			mWebView.destroy();
		super.onDestroy();
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

	@Override
	protected void setUpView() {
		super.setUpView();
		ToastUtils.showCenter(BrowserActivity.this,getResources().getString(R.string.brow));
	}

}
