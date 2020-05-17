package zd.cn.novipvideo.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.utils.ScreenUtil;

public class DialogLoading extends Dialog {
    @Bind(R.id.dialog_line)
    RelativeLayout dialogLine;
    @Bind(R.id.dialog_pro)
    ProgressBar dialogPro;
    private Activity activity;
    public DialogLoading(@NonNull Context context) {
        super(context, R.style.DialogLoading);
        activity = (Activity) context;
    }

    public DialogLoading(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DialogLoading(@NonNull Context context, boolean cancelable,  OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_dialog);
        ScreenUtil.setScreenData(activity);
        ButterKnife.bind(this);
        inint();
    }

    private void inint() {
        ScreenUtil.setRelativeLayoutParams(dialogPro,100,100,0,0,0,0);
        ScreenUtil.setLinearLayoutParams(dialogLine,300,300,0,0,0,0);
    }
}
