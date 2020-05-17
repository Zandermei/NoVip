//package zd.cn.novipvideo.ui;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.graphics.Rect;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.Toast;
//
//import com.tencent.bugly.crashreport.CrashReport;
//import com.tencent.smtt.sdk.TbsVideo;
//import com.xiaomi.market.sdk.UpdateResponse;
//import com.xiaomi.market.sdk.UpdateStatus;
//import com.xiaomi.market.sdk.XiaomiUpdateAgent;
//import com.xiaomi.market.sdk.XiaomiUpdateListener;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//
//import butterknife.Bind;
//import zd.cn.novipvideo.R;
//import zd.cn.novipvideo.adapter.MainAdapter;
//import zd.cn.novipvideo.utils.DataManagerUtils;
//import zd.cn.novipvideo.utils.ScreenUtil;
//import zd.cn.novipvideo.utils.StatusBarUtils;
//import zd.cn.novipvideo.utils.ToastUtils;
//import zd.cn.novipvideo.view.ImageTitleBar;
//
//public class MainActivity extends StatusBarActivity implements MainAdapter.OnSelectVideoClick{
//
//    @Bind(R.id.title_bar)
//    ImageTitleBar toolbar;
//    @Bind(R.id.recycView)
//    RecyclerView recyclerView;//列表布局
//    @Bind(R.id.ad_layout)
//    RelativeLayout adLayout;//广告显示
//
//
//    private MainAdapter recycViewAdapter;//列表适配器
//    private Context mContex;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    protected void setUpView() {
//        super.setUpView();
//        XiaomiUpdateAgent.update(this);
//    }
//
//    @Override
//    protected void initView() {
//        mContex = this;
//        toolbar.setTitle(DataManagerUtils.getCurentUser());
//        recycViewAdapter = new MainAdapter();
//        //创建布局管理器  LinerLayoutManager水平    GridLayoutManager网格  StaggeredGridLayoutManager瀑布流
//        GridLayoutManager manager = new GridLayoutManager(this,3);
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(recycViewAdapter);
//        recycViewAdapter.setOnSelectVideoItem(this);
//        ScreenUtil.setLinearLayoutParams(adLayout,0,460,0,0,0,0);//广告显示
//
//    }
//
//    @Override
//    protected void setLayout() {
//        StatusBarUtils.setStatusBarStyle(this,getResources().getColor(R.color.toorBar));
//        setContentView(R.layout.activity_main);
//    }
//
//    @Override
//    public void selectVideoItem(int pos) {
//        switch (pos){
//            case 0://腾讯
//                startActivity(new Intent(this,X5VideoActivity.class).putExtra("url",pos));
//                break;
//            case 1://优酷
//                startActivity(new Intent(this,X5VideoActivity.class).putExtra("url",pos));
//                break;
//            case 2://爱奇艺
//                startActivity(new Intent(this,X5VideoActivity.class).putExtra("url",pos));
//                break;
//            case 3://乐视
//                startActivity(new Intent(this,X5VideoActivity.class).putExtra("url",pos));
//                break;
//            case 4://芒果
//                startActivity(new Intent(this,X5VideoActivity.class).putExtra("url",pos));
//                break;
//            case 5://搜狐
//                startActivity(new Intent(this,X5VideoActivity.class).putExtra("url",pos));
//                break;
//        }
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        initBugly();//收集bug信息上报
//    }
//    private void initBugly() {
//        // 获取当前包名
//        String packageName = mContex.getPackageName();
//        // 获取当前进程名
//        String processName =getProcessName(android.os.Process.myPid());
//        // 设置是否为上报进程
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(mContex);
//        strategy.setUploadProcess(processName == null || processName.equals(packageName));
//        // 初始化Bugly
//        CrashReport.initCrashReport(mContex, "311526c204", false, strategy);
//        CrashReport.setUserId(DataManagerUtils.getCurentUser());
//
//    }
//    /**
//     * 获取进程号对应的进程名
//     *
//     * @param pid 进程号
//     * @return 进程名
//     */
//    private static String getProcessName(int pid) {
//        BufferedReader reader = null;
//        try {
//            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
//            String processName = reader.readLine();
//            if (!TextUtils.isEmpty(processName)) {
//                processName = processName.trim();
//            }
//            return processName;
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        } finally {
//            try {
//                if (reader != null) {
//                    reader.close();
//                }
//            } catch (IOException exception) {
//                exception.printStackTrace();
//            }
//        }
//        return null;
//    }
//}
