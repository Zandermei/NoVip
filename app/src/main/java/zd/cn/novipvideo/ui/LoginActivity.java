package zd.cn.novipvideo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.Bind;
import zd.cn.novipvideo.R;
import zd.cn.novipvideo.activity.MainActivity;
import zd.cn.novipvideo.utils.DataManagerUtils;
import zd.cn.novipvideo.utils.ScreenUtil;
import zd.cn.novipvideo.utils.StatusBarUtils;
import zd.cn.novipvideo.utils.ToastUtils;

public class LoginActivity extends StatusBarActivity implements View.OnClickListener{

    @Bind(R.id.login_btn)
    Button loginBtn;
    @Bind(R.id.login_ed)
    EditText loginEdit;
    @Bind(R.id.splash_logo)
    ImageView splashLogo;
    @Bind(R.id.splash_zt)
    ImageView splashZt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initView() {
        ScreenUtil.setRelativeLayoutParams(splashLogo,180,180,200,0,0,0);
        ScreenUtil.setRelativeLayoutParams(splashZt,0,150,10,30,90,90);
        ScreenUtil.setRelativeLayoutParams(loginEdit,0,100,150,60,70,70);
        ScreenUtil.setTextSize(loginEdit,32);
        ScreenUtil.setRelativeLayoutParams(loginBtn,150,150,20,0,0,0);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                String nike = loginEdit.getText().toString().trim();
                if(TextUtils.isEmpty(nike)){
                    ToastUtils.showCenter(LoginActivity.this,getResources().getString(R.string.login_edt));
                    return;
                }else {
                    DataManagerUtils.saveLoginCode(1);
                    DataManagerUtils.saveCurrentUser(nike);
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }
                break;
        }
    }
}
