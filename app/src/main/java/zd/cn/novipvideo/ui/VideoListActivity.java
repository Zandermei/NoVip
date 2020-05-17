package zd.cn.novipvideo.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.adapter.VideoListAdapter;
import zd.cn.novipvideo.model.VideoList;
import zd.cn.novipvideo.model.VideoNameModel;
import zd.cn.novipvideo.utils.AppLog;
import zd.cn.novipvideo.utils.OkHttpUtils;
import zd.cn.novipvideo.utils.ScreenUtil;
import zd.cn.novipvideo.utils.StatusBarUtils;
import zd.cn.novipvideo.utils.Url;
import zd.cn.novipvideo.view.ImageTitleBar;

/**
 * Created by zdd on 2019/7/15 0015.
 * 解析得到视频
 */

public class VideoListActivity extends StatusBarActivity implements View.OnClickListener,VideoListAdapter.OnclickItemListener{

    @Bind(R.id.progress_bar)
    ProgressWheel progressBar;
    @Bind(R.id.video_list)
    RecyclerView recyclerView;//视频列表
    @Bind(R.id.video_title_bar)
    ImageTitleBar titleBar;

    private String mIntentUrl;//接受传来的url，并解析
    private String titleName;//视频名称
    private List<VideoList> videoLists;
    private List<VideoNameModel.VideoList> videoNameList;//电影名称
    private int count = 0;//重试次数

    private VideoListAdapter listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setUpView() {
        Intent intent = getIntent();
        mIntentUrl = intent.getStringExtra("url");
        titleName = intent.getStringExtra("title");
        titleBar.setLeftImgVisibility(View.VISIBLE);
        titleBar.setTitle(titleName);
        titleBar.setLeftListener(this);
        //得到链接，开始解析
        setRequestVideo(mIntentUrl);

    }

    //发送一个请求
    private void setRequestVideo(final String mIntentUrl) {
        if(progressBar.getVisibility() == View.GONE){
            progressBar.setVisibility(View.VISIBLE);
        }
        String url = Url.urlJson[0] + mIntentUrl;
        OkHttpUtils.sendGetRequest(url, "", new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case OkHttpUtils.HTTP_REQUESTSUCESS:
                        JSONObject object = (JSONObject) msg.obj;
                        JSONArray array = object.optJSONArray("data");
                        videoLists = new ArrayList<>();
                        videoNameList = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            VideoNameModel model = new VideoNameModel();
                            try {
                                JSONObject tem = array.getJSONObject(i);
                                //添加电影名称，，电视剧只有一个，电影有多个
                                model.setVideoOneName(tem.optString("name"));

                                JSONObject object1 = tem.optJSONObject("source");
                                JSONArray array1 = object1.getJSONArray("eps");
                                for (int j = 0; j < array1.length(); j++) {
                                    JSONObject tem1 = array1.getJSONObject(j);
                                    String url = tem1.optString("url");
                                    if (!TextUtils.isEmpty(url) && url.equals("http://suo.im/5mRCVD")) {
                                        Toast.makeText(VideoListActivity.this, getResources().getString(R.string.error_line), Toast.LENGTH_SHORT).show();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressBar.setVisibility(View.GONE);
                                                finish();
                                            }
                                        },1500);//增加用户体验 延迟销毁页面
                                    } else {//得到集合显示
                                        VideoList list = new VideoList();
                                        list.setVideoName(tem1.optString("name"));
                                        list.setVideoUrl(tem1.optString("url"));
                                        videoLists.add(list);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        listAdapter = new VideoListAdapter(VideoListActivity.this,videoLists);
                        recyclerView.setLayoutManager(new LinearLayoutManager(VideoListActivity.this));//线性布局
                        recyclerView.setAdapter(listAdapter);
                        //设置分割线
                        recyclerView.addItemDecoration(new DividerItemDecoration(VideoListActivity.this,DividerItemDecoration.VERTICAL));
                        listAdapter.setOnclickItem(VideoListActivity.this);
                        progressBar.setVisibility(View.GONE);
                        break;
                    case OkHttpUtils.HTTP_RESPONSEFAIL:
                        if (count < 8) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setRequestVideo(mIntentUrl);
                                    count++;
                                }
                            }, 800);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(VideoListActivity.this, getResources().getString(R.string.line_qh), Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case OkHttpUtils.HTTP_REQUESTFAIL:
                        Toast.makeText(VideoListActivity.this, getResources().getString(R.string.net_error), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    @Override
    protected void initView() {
        ScreenUtil.setRelativeLayoutParams(progressBar, 100, 100, 0, 0, 0, 0);
    }

    @Override
    protected void setLayout() {
        StatusBarUtils.setStatusBarStyle(this,getResources().getColor(R.color.toorBar));
        setContentView(R.layout.activity_video);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_bar_img:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(progressBar != null){
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadingVideo(String url) {
        startActivity(new Intent(VideoListActivity.this,BrowserActivity.class).putExtra("url",url).putExtra("pos",OkHttpUtils.INTENT_CODE));
    }
}
