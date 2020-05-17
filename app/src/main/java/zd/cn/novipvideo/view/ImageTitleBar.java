package zd.cn.novipvideo.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.utils.ScreenUtil;

/**
 * Created by zdd on 2019/7/25 0025.
 */

public class ImageTitleBar extends RelativeLayout{

    @Bind(R.id.title_bar_layout)
    LinearLayout titleLayout;
    @Bind(R.id.title_bar_img)
    ImageView backImg;
    @Bind(R.id.title_bar_tv)
    TextView titleBar;
    @Bind(R.id.title_right)
    ImageView closeImg;
    @Bind(R.id.title_refsh)
    ImageView refshImg;//刷新

    private Activity mContext;

    public ImageTitleBar(Context context) {
        super(context);
        init(context,null);
    }

    public ImageTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public ImageTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = (Activity) context;
        LayoutInflater.from(mContext).inflate(R.layout.image_title_bar,this);
        ButterKnife.bind(this);
        ScreenUtil.setScreenData(mContext);
        ScreenUtil.setRelativeLayoutParams(titleLayout,0,90,0,0,15,15);
        ScreenUtil.setLinearLayoutParams(backImg,35,80,0,0,0,0);
        ScreenUtil.setLinearLayoutParams(closeImg,35,45,0,0,5,0);
        ScreenUtil.setLinearLayoutParams(refshImg,35,80,0,0,15,5);
        ScreenUtil.setLinearLayoutParams(titleBar,0,60,0,0,15,15);
        ScreenUtil.setTextSize(titleBar,32);
        //解析样式
        parseStyle(attrs,context);
    }

    private void parseStyle(AttributeSet attrs, Context context) {
        if(attrs != null){
            TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.ImageTitleBar);
            String title = array.getString(R.styleable.ImageTitleBar_imageTitle);
            titleBar.setText(title);
            Drawable leftDrawable = array.getDrawable(R.styleable.ImageTitleBar_image);
            if(leftDrawable != null){
                backImg.setImageDrawable(leftDrawable);
            }
            Drawable right = array.getDrawable(R.styleable.ImageTitleBar_closeImage);
            if(right != null){
                closeImg.setImageDrawable(right);
            }

            Drawable refsh = array.getDrawable(R.styleable.ImageTitleBar_refshImage);
            if(refsh != null){
                refshImg.setImageDrawable(refsh);
            }
            array.recycle();
        }
    }

    public void setTitle(String title){
        titleBar.setText(title);
    }

    public void setLeftImgVisibility(int vis){
        backImg.setVisibility(vis);
    }

    public void setRightImgVisibility(int vis){
        closeImg.setVisibility(vis);
    }

    public void setRefshImgVisibility(int vis){
        refshImg.setVisibility(vis);
    }

    public void setLeftImg(int id){
        backImg.setImageResource(id);
    }

    public void setLeftListener(OnClickListener listener){
        backImg.setOnClickListener(listener);
    }

    public void setRightListener(OnClickListener listener){
        closeImg.setOnClickListener(listener);
    }

    public void setRefshListener(OnClickListener listener){
        refshImg.setOnClickListener(listener);
    }

}
