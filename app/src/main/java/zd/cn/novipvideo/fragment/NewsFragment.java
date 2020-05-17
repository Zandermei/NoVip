package zd.cn.novipvideo.fragment;

import zd.cn.novipvideo.R;
import zd.cn.novipvideo.utils.AppLog;

/**
 * 新闻
 */
public class NewsFragment extends BaseFragment{
    @Override
    protected int setLayout() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setUpView() {

    }

    @Override
    public void onStop() {
        super.onStop();
        AppLog.e("news---onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        AppLog.e("news---onPause");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        AppLog.e("news---onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppLog.e("news---onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppLog.e("news---onDestroyView");
    }
}
