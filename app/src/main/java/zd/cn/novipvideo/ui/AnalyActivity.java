package zd.cn.novipvideo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.utils.AppLog;
import zd.cn.novipvideo.utils.OkHttpUtils;
import zd.cn.novipvideo.utils.StatusBarUtils;
import zd.cn.novipvideo.utils.ToastUtils;
import zd.cn.novipvideo.utils.Url;
import zd.cn.novipvideo.utils.UrlParams;
import zd.cn.novipvideo.view.ImageTitleBar;
import zd.cn.novipvideo.view.LoadingDialog;

/**
 * 解析video
 */
public class AnalyActivity extends StatusBarActivity{
    @Bind(R.id.analy_title)
    ImageTitleBar titleBar;
    @Bind(R.id.analy_tv)
    TextView analyTv;//错误提示

    private String intentTitle;
    private LoadingDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setLayout() {
        StatusBarUtils.setStatusBarStyle(this,getResources().getColor(R.color.toorBar));
        setContentView(R.layout.activity_analy);
    }

    @Override
    protected void initView() {
        titleBar.setLeftImgVisibility(View.VISIBLE);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        intentTitle = intent.getStringExtra("title");
        dialog = new LoadingDialog(this);
        if(TextUtils.isEmpty(url)){
            ToastUtils.showCenter(this,getResources().getString(R.string.back_ana));
            analyTv.setVisibility(View.VISIBLE);
        }else {
            analyTv.setVisibility(View.GONE);
            if(TextUtils.isEmpty(intentTitle)){
                titleBar.setTitle("畅看视频");
            }else {
                titleBar.setTitle(intentTitle);
            }
            //请求视频
            dialog.show();
            sendRequestVideo(url);
        }
    }

    @Override
    protected void setUpView() {
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendRequestVideo(String url) {
        String questUrl = Url.urlJson[0];

        OkHttpUtils.sendPostRequestVideo(questUrl, UrlParams.buildPostParams(url),new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case OkHttpUtils.HTTP_REQUESTSUCESS:
                        String result = (String) msg.obj;
                        //截取多余的字段 前41位 后两位截取掉
                        String jsonData = result.substring(41,(result.length() - 2));
                        try {
                            JSONObject object = new JSONObject(jsonData);
                            if(object.optString("success").equals("1")){
                               // String videoName = object.optString("title");
                                String videoUrl = object.optString("url");
                                dialog.dismiss();
                                startActivity(new Intent(AnalyActivity.this,VideoPlayAliActivity.class)
                                        .putExtra("title",intentTitle).putExtra("url",videoUrl));
                                finish();
                            }else {
                                analyTv.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                        break;

                    case OkHttpUtils.HTTP_RESPONSEFAIL:
                        dialog.dismiss();
                        ToastUtils.showCenter(AnalyActivity.this,getResources().getString(R.string.requst_error));
                        break;
                    case OkHttpUtils.HTTP_REQUESTFAIL:
                        dialog.dismiss();
                        ToastUtils.showCenter(AnalyActivity.this,getResources().getString(R.string.net_error));
                        break;
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
        super.onDestroy();
    }
}
