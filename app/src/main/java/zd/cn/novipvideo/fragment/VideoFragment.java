package zd.cn.novipvideo.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.activity.MainActivity;
import zd.cn.novipvideo.adapter.BannerAdapter;
import zd.cn.novipvideo.adapter.MainAdapter;
import zd.cn.novipvideo.model.AdParams;
import zd.cn.novipvideo.ui.ActivitySearchVideo;
import zd.cn.novipvideo.ui.TestVideoActivit;
import zd.cn.novipvideo.ui.X5VideoActivity;
import zd.cn.novipvideo.utils.AppLog;
import zd.cn.novipvideo.utils.DataManagerUtils;
import zd.cn.novipvideo.utils.ScreenUtil;
import zd.cn.novipvideo.view.ImageTitleBar;

/**
 * 视频
 */
public class VideoFragment extends BaseFragment implements MainAdapter.OnSelectVideoClick{

    @Bind(R.id.title_bar)
    ImageTitleBar toolbar;
    @Bind(R.id.recycView)
    RecyclerView recyclerView;//列表布局
    @Bind(R.id.ad_layout)
    RelativeLayout adLayout;//广告显示
    //广告轮播图
    @Bind(R.id.view_page)
    ViewPager viewPager;
    @Bind(R.id.view_point)
    LinearLayout mLinearLayout;

    private MainAdapter recycViewAdapter;//列表适配器
    private Context mContex;
    private List<ImageView> imgList;//广告图片集合
    private List<AdParams> adParams;//广告数据
    private BannerListener bannerListener;
    private int posionIndex = 0;//圆点初始位置

    private BannerAdapter bannerAdapter;//轮播适配器

    private final int PIC_STOP = 101;//停止循环
    private final int PIC_RESUME = 102;//开始循环


    @Override
    protected int setLayout() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView() {
        mContex = getContext();
        toolbar.setTitle("视频");
        recycViewAdapter = new MainAdapter();
        //创建布局管理器  LinerLayoutManager水平    GridLayoutManager网格  StaggeredGridLayoutManager瀑布流
        GridLayoutManager manager = new GridLayoutManager(mContex,3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(recycViewAdapter);
        recycViewAdapter.setOnSelectVideoItem(this);
        ScreenUtil.setLinearLayoutParams(adLayout,0,460,0,0,0,0);//广告显示
        //初始化广告
        initAD();

    }

    private void initAD() {
        //获取广告信息
        adParams = getAdParams();
        imgList = new ArrayList<>();
        View view;
        LayoutParams params;
        for(int i = 0;i<adParams.size();i++){
            //设置广告图片
            ImageView imageView = new ImageView(mContex);//创建显示图片的容器
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置图片显示模式
            imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
            Glide.with(mContex).load(adParams.get(i).getPicture()).placeholder(R.mipmap.default_pic).into(imageView);
            imgList.add(imageView);
            //设置广告圆点
            view = new View(mContex);
            params = new LayoutParams(ScreenUtil.getRealWidth(15),ScreenUtil.getRealWidth(15));
            params.leftMargin = ScreenUtil.getRealWidth(10);
            params.bottomMargin = ScreenUtil.getRealWidth(15);
            view.setBackgroundResource(R.drawable.point_background);
            view.setLayoutParams(params);
            view.setEnabled(false);
            mLinearLayout.addView(view);//将圆点组价添加到布局
        }
        //设置滑动适配器
        bannerAdapter = new BannerAdapter(adParams,imgList);
        viewPager.setAdapter(bannerAdapter);
        //设置圆点初始位置,及滑动监听
        initAction();
    }

    private void initAction() {
        bannerListener = new BannerListener();
        viewPager.setOnPageChangeListener(bannerListener);
        viewPager.setCurrentItem(Integer.MAX_VALUE/2);
        mLinearLayout.getChildAt(posionIndex).setEnabled(true);
    }

    /**
     * 轮播图滑动监听
     */
    private class BannerListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //根据图片改变圆点的状态
            int newPosion = position % imgList.size();
            mLinearLayout.getChildAt(newPosion).setEnabled(true);
            mLinearLayout.getChildAt(posionIndex).setEnabled(false);
            //更新选择状态
            posionIndex = newPosion;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state){
                case ViewPager.SCROLL_STATE_DRAGGING://滚动状态
                    handler.sendEmptyMessage(PIC_STOP);
                    break;

                case ViewPager.SCROLL_STATE_IDLE://滚动空闲
                    AppLog.e("滚动空闲---");
                    handler.sendEmptyMessageDelayed(PIC_RESUME,2000);
                    break;
            }
        }
    }

    @Override
    protected void setUpView(){
        //广告自动滑动
        handler.sendEmptyMessageDelayed(PIC_RESUME,2000);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(handler.hasMessages(PIC_RESUME)){
                handler.removeMessages(PIC_RESUME);
            }
            switch (msg.what){
                case PIC_RESUME://滑动
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                    handler.sendEmptyMessageDelayed(PIC_RESUME,2000);
                    break;

                case PIC_STOP://停止滑动
                    //什么都不做
                    break;
            }
        }
    };

    @Override
    public void selectVideoItem(int pos) {
        switch (pos){
            case 0://腾讯
                startActivity(new Intent(mContex,X5VideoActivity.class).putExtra("url",pos));
                break;
            case 1://优酷
                startActivity(new Intent(mContex,X5VideoActivity.class).putExtra("url",pos));
                break;
            case 2://爱奇艺
                startActivity(new Intent(mContex,X5VideoActivity.class).putExtra("url",pos));
                break;
            case 3://乐视
                startActivity(new Intent(mContex,X5VideoActivity.class).putExtra("url",pos));
                break;
            case 4://芒果
                startActivity(new Intent(mContex,X5VideoActivity.class).putExtra("url",pos));
                break;
            case 5://搜狐
//                startActivity(new Intent(mContex,X5VideoActivity.class).putExtra("url",pos));
                startActivity(new Intent(mContex,ActivitySearchVideo.class));
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isPer = false;
        if(grantResults.length > 0){
            for (int i=0;i<grantResults.length;i++){
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    isPer = true;
                }else {
                    isPer = false;
                }
            }
        }

        switch (requestCode){
            case 10:
                if(!isPer){
                    Toast.makeText(mContex,"权限不足",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public List<AdParams> getAdParams() {
        adParams = new ArrayList<>();
        AdParams params = new AdParams();
        params.setPicture("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1567412093&di=d89919e38d3d939e3c34b80548e8860d&imgtype=jpg&er=1&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20161018%2F14ccb02fffea480797124b104431769e_th.jpeg");
        adParams.add(params);

        AdParams params1 = new AdParams();
        params1.setPicture("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1567412111&di=0daf230627bc687aa532abd285a4ea29&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201203%2F05%2F20120305205212_MNNcA.jpeg");
        adParams.add(params1);

        AdParams params2 = new AdParams();
        params2.setPicture("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566817446196&di=512dd5518f0fc92092faf3ed8cae6342&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201609%2F09%2F20160909150723_r4uYe.thumb.700_0.jpeg");
        adParams.add(params2);

        AdParams params3 = new AdParams();
        params3.setPicture("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566822855026&di=bcdc9fc1e287c337056b919e424bd39c&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20120625%2FImg346436473.jpg");
        adParams.add(params3);

        AdParams params4 = new AdParams();
        params4.setPicture("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1566812788&di=e2967a0d0af5a5089d18a91fe1663f72&src=http://img.pconline.com.cn/images/upload/upc/tx/photoblog/1107/25/c1/8434785_8434785_1311557396687.jpg");
        adParams.add(params4);
        return adParams;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //bannerAdapter.notifyDataSetChanged();
    }
}
