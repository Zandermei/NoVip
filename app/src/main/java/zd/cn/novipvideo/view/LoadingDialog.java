package zd.cn.novipvideo.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.utils.ScreenUtil;

public class LoadingDialog extends Dialog{
    @Bind(R.id.dialog_bg)
    RelativeLayout relativeLayout;
    @Bind(R.id.dialog_tv)
    TextView dialogTv;
    private Activity activity;

    public LoadingDialog(@NonNull Context context) {
        super(context,R.style.loading);
        activity = (Activity) context;

    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        ButterKnife.bind(this);
        ScreenUtil.setScreenData(activity);
        initView();
    }

    private void initView() {
        ScreenUtil.setLinearLayoutParams(relativeLayout,260,260,0,0,0,0);
        ScreenUtil.setRelativeLayoutParams(dialogTv,0,50,15,0,0,0);
        ScreenUtil.setTextSize(dialogTv,26);
    }
}
