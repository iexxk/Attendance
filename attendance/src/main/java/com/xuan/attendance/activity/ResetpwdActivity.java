package com.xuan.attendance.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.attendance.BaseActivity;
import com.xuan.attendance.R;
import com.xuan.attendance.function.BmobUserMsg;
import com.xuan.attendance.mApplication;
import com.xuan.attendance.util.LogUtils;
import com.xuan.attendance.util.ViewField;
/**
 * Created by Administrator on 2016/1/15.
 */
public class ResetpwdActivity extends BaseActivity implements View.OnClickListener {
    /**邮箱的icon*/
    @ViewField(R.id.email_icon_forget)
    ImageView mEmail_icon_forget;
    /**邮箱输入框*/
    @ViewField(R.id.email_forget)
    EditText mEmail_forget;
    /**找回密码按钮*/
    @ViewField(R.id.resetpwd)
    TextView mResetpwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_forgetpassword);
        try {
            ViewField.Processor.process(this);
            mResetpwd.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("TAG","load view error");
        }
    }
    @Override
    public void onClick(View v) {
        BmobUserMsg bmobUserMsg=new BmobUserMsg(this);
        bmobUserMsg.restpwd(mEmail_forget);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.back_in_left, R.anim.back_out_right);
    }
}
