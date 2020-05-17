package zd.cn.novipvideo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.load.model.GlideUrl;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import zd.cn.novipvideo.R;

import zd.cn.novipvideo.model.MovModel;
import zd.cn.novipvideo.utils.AppLog;

import zd.cn.novipvideo.utils.GlideUtils;
import zd.cn.novipvideo.utils.ScreenUtil;

public class AdapterMov extends RecyclerView.Adapter<AdapterMov.ViewHolder> {

    List<MovModel> modelList;

    public
    AdapterMov(List<MovModel> model) {
        this.modelList = model;
    }


    @NonNull
    @Override
    public AdapterMov.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mov, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMov.ViewHolder holder, int position) {
        final MovModel movModel = modelList.get(position);
        holder.movLeftName.setText(movModel.getVod_remarks());
        holder.movRightDesc.setText("简介："+movModel.getVod_content());
        holder.movRightArea.setText("地区："+movModel.getVod_area());
        holder.movRightName.setText(movModel.getVod_name());
        GlideUtils.GlideLoadImg(movModel.getVod_pic(),holder.movLeftImg);
        holder.movRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mov != null){
                    mov.PlayUrlList(movModel.getVod_play_url());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList == null ? 0 : modelList.size();
    }

    public void setData(List<MovModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.mov_root)
        LinearLayout movRoot;
        @Bind(R.id.mov_left_img)
        ImageView movLeftImg;
        @Bind(R.id.mov_left_line)
        RelativeLayout movLeftLine;
        @Bind(R.id.mov_right_line)
        LinearLayout movRightLine;
        @Bind(R.id.mov_right_name)
        TextView movRightName;
        @Bind(R.id.mov_right_area)
        TextView movRightArea;
        @Bind(R.id.mov_right_desc)
        TextView movRightDesc;
        @Bind(R.id.mov_left_name)
        TextView movLeftName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            ScreenUtil.setRelativeLayoutParams(movRoot,0,360,10,0,0,0);
            ScreenUtil.setLinearLayoutParams(movLeftLine,250,360,0,0,15,15);
            ScreenUtil.setRelativeLayoutParams(movLeftImg,245,350,0,0,0,0);
            ScreenUtil.setPadding(movLeftName,5,5,10,10);
            ScreenUtil.setTextSize(movLeftName,24);
            //right
            ScreenUtil.setLinearLayoutParams(movRightLine,0,330,0,0,0,0);
            ScreenUtil.setPadding(movRightName,5,5,10,10);
            ScreenUtil.setTextSize(movRightName,32);
            ScreenUtil.setPadding(movRightArea,5,5,10,10);
            ScreenUtil.setTextSize(movRightArea,28);
            ScreenUtil.setPadding(movRightDesc,5,5,10,10);
            ScreenUtil.setTextSize(movRightDesc,26);

        }
    }

    public interface OnSelectMovClick{
        void PlayUrlList(String urlList);
    }
    private OnSelectMovClick mov;
    public void setOnSelectMov(OnSelectMovClick click){
        this.mov = click;
    }
}
