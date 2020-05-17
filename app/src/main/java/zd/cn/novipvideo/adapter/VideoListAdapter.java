package zd.cn.novipvideo.adapter;

import android.content.Context;
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
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.model.VideoList;
import zd.cn.novipvideo.utils.AppLog;
import zd.cn.novipvideo.utils.ScreenUtil;

/**
 * Created by Administrator on 2019/7/25 0025.
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoHolder>{

    private Context mContex;
    private List<VideoList> dataList;
    private LayoutInflater inflater;
    private OnclickItemListener onclickLoading;

    public VideoListAdapter(Context context,List<VideoList> videoLists){
        this.dataList = videoLists;
        this.mContex = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public VideoListAdapter.VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_video,parent,false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoListAdapter.VideoHolder holder, int position) {
        final VideoList list = dataList.get(position);
        holder.videoName.setText(list.getVideoName());
        holder.videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onclickLoading != null){
                    onclickLoading.loadingVideo(list.getVideoUrl());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class VideoHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.video_item_layout)
        LinearLayout videoLayout;
        @Bind(R.id.video_item_img)
        ImageView videoImg;//视频图片
        @Bind(R.id.video_item_tv)
        TextView videoName;//视频名称

        public VideoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            ScreenUtil.setLinearLayoutParams(videoLayout,0,200,0,0,0,0);
            ScreenUtil.setLinearLayoutParams(videoImg,235,165,0,0,20,10);
            ScreenUtil.setLinearLayoutParams(videoName,0,100,0,0,15,0);
            ScreenUtil.setTextSize(videoName,35);
        }
    }

    public interface OnclickItemListener{
        void loadingVideo(String url);
    }
    public void setOnclickItem(OnclickItemListener onclickLoading){
        this.onclickLoading = onclickLoading;
    }
}
