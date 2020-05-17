package zd.cn.novipvideo.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment{
    public Activity activity;
    public Context mContext;
    private View mRootView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
        this.mContext = context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(mRootView == null) {
            mRootView = inflater.inflate(setLayout(), container, false);
            ButterKnife.bind(this, mRootView);
            initView();
            setUpView();
        }

        // 缓存的mRootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null)
        {
            parent.removeView(mRootView);
        }

        return mRootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mRootView = null;
    }

    protected abstract int setLayout();
    protected abstract void initView();
    protected abstract void setUpView();
}
