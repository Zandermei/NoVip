package zd.cn.novipvideo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.utils.ScreenUtil;

/**
 * Created by Administrator on 2019/7/17 0017.
 */

public class SpinnerAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private int[] selectData = {R.string.line1,R.string.line2,R.string.line3,R.string.line4
           ,R.string.line5,R.string.line6,R.string.line7,R.string.line8,R.string.line9,R.string.line10};

    public SpinnerAdapter(Context context){
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return selectData.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_spinner,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.itemTv.setText(selectData[position]);
        return convertView;
    }

    public class ViewHolder{
        @Bind(R.id.spinner_item)
        TextView itemTv;
        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
            ScreenUtil.setLinearLayoutParams(itemTv,0,75,0,0,0,0);
            ScreenUtil.setTextSize(itemTv,30);
        }
    }
}
