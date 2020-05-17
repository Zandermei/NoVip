package zd.cn.novipvideo.ui;

import android.os.Build;

import com.aliyun.utils.VcPlayerLog;

import zd.cn.novipvideo.R;
import zd.cn.novipvideo.utils.StatusBarUtils;

/**
 * Created by Administrator on 2019/7/15 0015.
 */

public class StatusBarActivity extends BaseActivity{
    @Override
    protected void setUpView() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setLayout() {

    }

    @Override
    protected boolean beforeLayout() {
        
        return true;
    }

    protected boolean isStrangePhone() {
        boolean strangePhone = "mx5".equalsIgnoreCase(Build.DEVICE)
                || "Redmi Note2".equalsIgnoreCase(Build.DEVICE)
                || "Z00A_1".equalsIgnoreCase(Build.DEVICE)
                || "hwH60-L02".equalsIgnoreCase(Build.DEVICE)
                || "hermes".equalsIgnoreCase(Build.DEVICE)
                || ("V4".equalsIgnoreCase(Build.DEVICE) && "Meitu".equalsIgnoreCase(Build.MANUFACTURER))
                || ("m1metal".equalsIgnoreCase(Build.DEVICE) && "Meizu".equalsIgnoreCase(Build.MANUFACTURER));

        VcPlayerLog.e("lfj1115 ", " Build.Device = " + Build.DEVICE + " , isStrange = " + strangePhone);
        return strangePhone;
    }
}
