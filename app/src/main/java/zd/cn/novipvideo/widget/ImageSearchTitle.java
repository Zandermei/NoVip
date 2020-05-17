package zd.cn.novipvideo.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.transition.Scene;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.utils.ScreenUtil;

public class ImageSearchTitle extends RelativeLayout {
    @Bind(R.id.title_layout)
    LinearLayout linearLayout;
    @Bind(R.id.title_back_img)
    ImageView titleBack;
    @Bind(R.id.title_ed)
    EditText titleEd;
    @Bind(R.id.title_tv)
    TextView titleTv;



    private Activity activity;

    public ImageSearchTitle(Context context) {
        super(context);
        inits(context,null);
    }

    public ImageSearchTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        inits(context,attrs);
    }

    public ImageSearchTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inits(context,attrs);
    }

    private void inits(Context context, AttributeSet attrs) {
        activity = (Activity) context;
        LayoutInflater.from(context).inflate(R.layout.widget_search_title,this);
        ButterKnife.bind(this);
        ScreenUtil.setScreenData(activity);
        ScreenUtil.setRelativeLayoutParams(linearLayout,0,90,0,0,0,0);
        ScreenUtil.setLinearLayoutParams(titleEd,0,65,0,0,15,15);
        ScreenUtil.setLinearLayoutParams(titleBack,55,45,0,0,15,0);
        ScreenUtil.setPadding(titleTv,5,5,15,15);
        ScreenUtil.setTextSize(titleTv,30);
        ScreenUtil.setTextSize(titleEd,30);

        parseTitle(context,attrs);

    }

    private void parseTitle(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.ImageSearchTitle);
        String string = array.getString(R.styleable.ImageSearchTitle_title_search);
        if(null != string){
           titleEd.setText(string);
        }

    }

    public void setOnSearchClick(OnClickListener listener){
        titleTv.setOnClickListener(listener);
    }

    public void setLeftClickListener(OnClickListener leftClickListener){
        titleBack.setOnClickListener(leftClickListener);
    }

    public String getSearchMsg(){
        return titleEd.getText().toString();
    }
}
