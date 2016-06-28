package com.xuan.attendance.activity;

import android.content.Intent;
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
public class SignupActivity extends BaseActivity implements View.OnClickListener{
    private Intent intent;
    private String TAG = "SignupActivity";
    /**邮箱的icon*/
    @ViewField(R.id.email_icon_signup)
    ImageView mEmail_icon_signup;
    /**邮箱的输入框*/
    @ViewField(R.id.email_signup)
    EditText mEmail_signup;
    /**邮箱的icon*/
    @ViewField(R.id.password_icon_signup)
    ImageView mPassword_icon_signup;
    /**邮箱的输入框*/
    @ViewField(R.id.password_signup)
    EditText mPassword_signup;
    /**重复密码的icon*/
    @ViewField(R.id.password_icon1)
    ImageView mPassword_icon1;
    /**重复密码的输入框*/
    @ViewField(R.id.password_signup_again)
    EditText mPassword_signup_again;
    /**登陆的按钮*/
    @ViewField(R.id.signup)
    TextView mSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mApplication.getInstance().addActivity(this);
        try {
            ViewField.Processor.process(this);
            mSignup.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG,"load view error");
        }
    }
    @Override
    public void onClick(View v) {
        BmobUserMsg bmobUserMsg=new BmobUserMsg(this);
        bmobUserMsg.SignUp( mEmail_signup, mPassword_signup, mPassword_signup_again);
    }
    /**
     * 此Activity的退出执行的动画
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.back_in_left, R.anim.back_out_right);
    }
}
