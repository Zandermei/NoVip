package zd.cn.novipvideo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import zd.cn.novipvideo.NoVipApplication;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.utils.ScreenUtil;

/**
 * Created by Administrator on 2019/7/15 0015.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.Viewholder> {

    private OnSelectVideoClick selectVideoClick;

    private int[] videoName = {R.string.tencent,R.string.youku,R.string.aqiyi,
                                R.string.leshitv,R.string.mgtv,R.string.sohu};
    private int[] videoPic = {R.mipmap.tenxun_t,R.mipmap.youku_t,R.mipmap.aiqy,
            R.mipmap.leshi_t,R.mipmap.ma_tv,R.mipmap.souhu_t};

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(NoVipApplication.getApplicationContex()).inflate(R.layout.item_main_adapter,parent,false);
        return new Viewholder(view);
    }

    /**
     * 组件显示内容
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(Viewholder holder, final int position) {
        holder.vdeoIamge.setBackgroundResource(videoPic[position]);
        holder.videoName.setText(videoName[position]);
        holder.videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectVideoClick != null){
                    selectVideoClick.selectVideoItem(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoName.length;
    }

    /**
     * 创建viewholder 查找组件
     */
    public class Viewholder extends RecyclerView.ViewHolder{
        @Bind(R.id.video_layout)
        LinearLayout videoLayout;
        @Bind(R.id.videoImg)
        ImageView vdeoIamge;//视频图标
        @Bind(R.id.videoName)
        TextView videoName;//视频名称
        public Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            ScreenUtil.setLinearLayoutParams(videoLayout,200,200,10,10,0,0);
            ScreenUtil.setLinearLayoutParams(vdeoIamge,100,100,0,0,0,0);
            ScreenUtil.setLinearLayoutParams(videoName,0,45,5,0,0,0);
            ScreenUtil.setTextSize(videoName,30);
        }
    }

    public interface OnSelectVideoClick{
        void selectVideoItem(int pos);
    }
    public void setOnSelectVideoItem(OnSelectVideoClick click){
        this.selectVideoClick = click;
    }
}
