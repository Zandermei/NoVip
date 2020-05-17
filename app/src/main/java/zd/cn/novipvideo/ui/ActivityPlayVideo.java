//package zd.cn.novipvideo.ui;
//
//import android.app.AliasActivity;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.graphics.PixelFormat;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.view.SurfaceHolder;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.MediaController;
//;
//
//
//import com.aliyun.player.AliPlayer;
//import com.aliyun.player.AliPlayerFactory;
//import com.aliyun.player.IPlayer;
//import com.aliyun.player.bean.ErrorInfo;
//import com.aliyun.player.source.UrlSource;
//import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
//import com.tencent.smtt.sdk.WebChromeClient;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Pattern;
//
//import zd.cn.novipvideo.R;
//
//import zd.cn.novipvideo.model.MovModel;
//import zd.cn.novipvideo.utils.AppLog;
//import zd.cn.novipvideo.view.DialogLoading;
//
//public class ActivityPlayVideo extends StatusBarActivity {
//
//
//    private String play_url;
//    private List<MovModel.VodUrl> vodUrlList;
//    private DialogLoading loading;
//    private X5WebView mWebView;
//    private AliPlayer mPlayer;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().setFormat(PixelFormat.TRANSLUCENT);
//    }
//
//    @Override
//    protected void setLayout() {
//        setContentView(binding.getRoot());
//        play_url = getIntent().getStringExtra("url");
//    }
//
//    @Override
//    protected void initView() {
//        vodUrlList = new ArrayList<>();
//        loading = new DialogLoading(this);
//
//
//
//     //  initWeb();
//
//
//    }
//
////    private void initWeb() {
////        mWebView = new X5WebView(this);
////        binding.fragment.addView(mWebView, new FrameLayout.LayoutParams(
////                FrameLayout.LayoutParams.FILL_PARENT,
////                FrameLayout.LayoutParams.FILL_PARENT));
////
////        mWebView.setWebChromeClient(new WebChromeClient(){
////            View myVideoView;
////            View myNormalView;
////            IX5WebChromeClient.CustomViewCallback callback;
////            /**
////             * 全屏播放配置
////             */
////            @Override
////            public void onShowCustomView(View view,
////                                         IX5WebChromeClient.CustomViewCallback customViewCallback) {
////                FrameLayout normalView = findViewById(R.id.web_filechooser);
////                ViewGroup viewGroup = (ViewGroup) normalView.getParent();
////                viewGroup.removeView(normalView);
////                viewGroup.addView(view);
////                myVideoView = view;
////                myNormalView = normalView;
////                callback = customViewCallback;
////            }
////
////            @Override
////            public void onHideCustomView() {
////                if (callback != null) {
////                    callback.onCustomViewHidden();
////                    callback = null;
////                }
////                if (myVideoView != null) {
////                    ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
////                    viewGroup.removeView(myVideoView);
////                    viewGroup.addView(myNormalView);
////                }
////            }
////
////        });
////    }
//
//    @Override
//    protected void setUpView() {
//       // loading.show();
//        dowill();
//    }
//
//
//    //处理链接
//    private void dowill() {
//        //分割字符创
//        Pattern pattern = Pattern.compile("\\$\\$\\$");
//        Pattern pattern1 = Pattern.compile("#");
//        String[] url = pattern.split(play_url);//只取第一类数据
//        String[] urlRelay = pattern1.split(url[0]);
//        for (int j = 0; j < urlRelay.length; j++) {
//            MovModel.VodUrl vodUrl = new MovModel.VodUrl();
//            vodUrl.setVod_play_url(urlRelay[j].substring(urlRelay[j].indexOf("$") + 1, urlRelay[j].length()));
//            vodUrl.setVod_play_title(urlRelay[j].substring(0, urlRelay[j].indexOf("$")));
//            vodUrlList.add(vodUrl);
//        }
//
//        startActivity(new Intent(this,VideoPlayAliActivity.class).putExtra("url",vodUrlList.get(0).getVod_play_url()));
//    }
//
//
//    @Override
//    public void onConfigurationChanged(@NonNull Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//    }
//
//
//
//}
