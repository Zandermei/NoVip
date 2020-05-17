package zd.cn.novipvideo.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.Bind;
import okhttp3.Call;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.adapter.AdapterMov;

import zd.cn.novipvideo.model.MovModel;
import zd.cn.novipvideo.utils.AppLog;
import zd.cn.novipvideo.utils.CallBackUtil;
import zd.cn.novipvideo.utils.OkHttpUtil;
import zd.cn.novipvideo.utils.OkHttpUtils;
import zd.cn.novipvideo.utils.ToastUtils;
import zd.cn.novipvideo.utils.Url;
import zd.cn.novipvideo.view.DialogLoading;
import zd.cn.novipvideo.widget.ImageSearchTitle;

public class ActivitySearchVideo extends StatusBarActivity implements View.OnClickListener, AdapterMov.OnSelectMovClick {

    @Bind(R.id.title_search)
    ImageSearchTitle titleSearch;
    @Bind(R.id.recy_view)
    RecyclerView recyView;

    private DialogLoading dialogLoading;


    private List<MovModel> modelList;
    private List<MovModel.VodUrl> vodUrlList;
    private AdapterMov adapterMov;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_search_video);
    }

    @Override
    protected void initView() {
        dialogLoading = new DialogLoading(this);
        titleSearch.setLeftClickListener(this);
        titleSearch.setOnSearchClick(this);

    }

    @Override
    protected void setUpView() {
        dialogLoading = new DialogLoading(this);
        adapterMov = new AdapterMov(modelList);
        recyView.setLayoutManager(new LinearLayoutManager(this));
        recyView.setAdapter(adapterMov);
        adapterMov.setOnSelectMov(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_img:
                finish();
                break;

            case R.id.title_tv:
//                if (EaseCommonUtils.isNetWorkConnected(this)) {
                   dialogLoading.show();
//                    dialogLoading.setCancelable(false);
//                    modelList = null;
//                    adapterMov.setData(modelList);
//                    searchMov(titleSearch.getSearchMsg());
//                    CommentUtils.hideKeyboard(this);
//                } else {
//                    ToastUtils.showCenter(this,getResources().getString(R.string.net_error));
//                    return;
//                }
                searchMov(titleSearch.getSearchMsg());
                break;
        }
    }

    private void searchMov(String searchMsg) {
        String url = Url.searchUrl_video;
        AppLog.e("搜索的名称：：："+searchMsg);
        OkHttpUtil.okHttpGet(url, Url.buildGetMovParams(searchMsg), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                ToastUtils.showToast(ActivitySearchVideo.this,"请求失败");
                dialogLoading.dismiss();
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    AppLog.e("------"+response);
                    if (object.optString("Code").equals("200")) {
                        modelList = new ArrayList<>();
                        vodUrlList = new ArrayList<>();
                        JSONArray array = object.getJSONArray("Data");
                        if (array != null || !array.equals("[]")) {
                            for (int i = 0; i < array.length(); i++) {
                                MovModel movModel = new MovModel();
                                JSONObject temp = array.getJSONObject(i);
                                movModel.setVod_name(temp.optString("vod_name"));
                                movModel.setVod_actor(temp.optString("vod_actor"));
                                movModel.setVod_area(temp.optString("vod_area"));
                                movModel.setVod_remarks(temp.optString("vod_remarks"));
                                movModel.setVod_pic(temp.optString("vod_pic"));
                                movModel.setVod_year(temp.optString("vod_year"));
                                movModel.setVod_content(temp.optString("vod_content"));
                                movModel.setVod_play_url(temp.optString("vod_play_url"));//
                                modelList.add(movModel);
                            }
                        }
                    }else {
                        ToastUtils.showCenter(ActivitySearchVideo.this,getResources().getString(R.string.data_error));
                        dialogLoading.dismiss();
                    }
                    setAdapter();
                    dialogLoading.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private void setAdapter() {
        adapterMov.setData(modelList);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void PlayUrlList(String urlList) {
        startActivity(new Intent(this, VideoPlayAliActivity.class).putExtra("url", urlList));
    }

}
