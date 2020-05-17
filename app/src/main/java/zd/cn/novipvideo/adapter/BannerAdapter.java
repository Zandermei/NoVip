package zd.cn.novipvideo.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import zd.cn.novipvideo.model.AdParams;
import zd.cn.novipvideo.utils.AppLog;

public class BannerAdapter extends PagerAdapter{

    private List<AdParams> adParams;
    private List<ImageView> imgList;

    public BannerAdapter(List<AdParams> params, List<ImageView> imageViewList) {
        this.adParams = params;
        this.imgList = imageViewList;
    }

    @Override
    public int getCount() {
        return imgList == null || imgList.isEmpty() ? 0 :Integer.MAX_VALUE;//1修改取超大的数，实现无线循环效果
       // return imgList == null || imgList.isEmpty() ? 0 :imgList.size();////取超大的数，实现无线循环效果
        //当滑动到最后一个图片，在此滑动会循环到第一个
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //2修改，当前要显示的数据索引为集合长度
      int newPos = position % imgList.size();
       container.addView(imgList.get(newPos));
//        ImageView imageView = imgList.get(position);
//        container.addView(imageView);
//        //图片点击事件
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // params.getPicUrl();
//            }
//        });
        return imgList.get(newPos);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //3修改，移除的数据索引为集合的长度
        int newPos = position%imgList.size();
        container.removeView(imgList.get(newPos));
        //container.removeView((View) object);
    }


}
