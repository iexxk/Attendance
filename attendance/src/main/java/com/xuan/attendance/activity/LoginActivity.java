package com.xuan.attendance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.isnc.facesdk.SuperID;
import com.isnc.facesdk.common.Cache;
import com.isnc.facesdk.common.SDKConfig;
import com.xuan.attendance.BaseActivity;
import com.xuan.attendance.R;
import com.xuan.attendance.function.BmobUserMsg;
import com.xuan.attendance.mApplication;
import com.xuan.attendance.util.LogUtils;
import com.xuan.attendance.util.ViewField;

/**
 * Created by Administrator on 2016/1/15.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = "LoginActivity";

    /**
     * 邮箱的icon
     */
    @ViewField(R.id.email_icon_login)
    ImageView mEmail_icon_login;
    /**
     * 邮箱的输入框
     */
    @ViewField(R.id.email_login)
    EditText mEmail_login;
    /**
     * 密码的icon
     */
    @ViewField(R.id.password_icon_login)
    ImageView mPassword_icon_login;
    /**
     * 密码的输入框
     */
    @ViewField(R.id.password_login)
    EditText mPassword_login;
    /**
     * 注册按钮
     */
    @ViewField(R.id.signup_login_page)
    TextView mSignup_login_page;
    /**
     * 忘记密码按钮
     */
    @ViewField(R.id.forgetpwd)
    TextView mForgetpwd;
    /**
     * 登陆按钮
     */
    @ViewField(R.id.login)
    TextView mLogin;
    /**
     * 刷脸登陆按钮
     */
    @ViewField(R.id.superidlogin)
    TextView mSuperidlogin;


    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_login);
        try {
            ViewField.Processor.process(this);
            mLogin.setOnClickListener(this);
            mSignup_login_page.setOnClickListener(this);
            mForgetpwd.setOnClickListener(this);
            mSuperidlogin.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("TAG", "load view error");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                Login();
                break;
            case R.id.signup_login_page:
                intent = new Intent(this, SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_right, R.anim.out_left);
                break;
            case R.id.forgetpwd:
                intent = new Intent(this, ResetpwdActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_right, R.anim.out_left);
                break;
            case R.id.superidlogin:
                SuperidLogin();
                break;
            default:
                break;
        }
    }

    private void SuperidLogin() {
        BmobUserMsg bmobUserMsg = new BmobUserMsg(this);
        bmobUserMsg.SuperidLogin();
    }


    private void Login() {
        BmobUserMsg bmobUserMsg = new BmobUserMsg(this);
        bmobUserMsg.Login(mEmail_login, mPassword_login);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            // 授权成功
            case SDKConfig.AUTH_SUCCESS:
                Toast.makeText(LoginActivity.this, "未绑定,请先登录或注册,在菜单里绑定", Toast.LENGTH_LONG).show();
                if (!Cache.getCached(LoginActivity.this, SDKConfig.KEY_ACCESSTOKEN).equals("")) {
                    SuperID.userCancelAuthorization(LoginActivity.this, new SuperID.IntSuccessCallback() {
                        @Override
                        public void onSuccess(int arg0) {
                            // TODO 处理成功事件
                        }
                    }, new SuperID.IntFailCallback() {
                        @Override
                        public void onFail(int arg0) {
                            // TODO 处理失败事件
                            //int arg0为返回错误类型，若需详细处理返回错误类型，则相应地错误码如下：
                            switch (arg0) {
                                case SDKConfig.ACCESSTOKEN_EXPIRED:
                                    Toast.makeText(LoginActivity.this, "解绑失败,access_token过期", Toast.LENGTH_SHORT).show();
                                    break;
                                case SDKConfig.OTHER_ERROR:
                                    Toast.makeText(LoginActivity.this, "解绑失败,网络连接错误", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });

                }
                Cache.clearCached(LoginActivity.this);//清楚所有数据
                break;
            // 找不到该用户
            case SDKConfig.USER_NOTFOUND:
                Toast.makeText(LoginActivity.this, "找不到该用户", Toast.LENGTH_SHORT).show();
                Cache.clearCached(LoginActivity.this);//清楚所有数据
                break;
            // 登录成功
            case SDKConfig.LOGINSUCCESS:
                Toast.makeText(LoginActivity.this, "人脸识别成功，正在登录，请稍后", Toast.LENGTH_SHORT).show();
                String uid = Cache.getCached(LoginActivity.this, SDKConfig.KEY_UID);
                BmobUserMsg bmobUserMsg1 = new BmobUserMsg(this);
                bmobUserMsg1.Login(uid);
                break;
            // 登录失败
            case SDKConfig.LOGINFAIL:
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                Cache.clearCached(LoginActivity.this);//清楚所有数据
                break;
            // 网络有误
            case SDKConfig.NETWORKFAIL:
                Toast.makeText(LoginActivity.this, "网络有误", Toast.LENGTH_SHORT).show();
                Cache.clearCached(LoginActivity.this);//清楚所有数据
                break;
            // 一登SDK版本过低
            case SDKConfig.SDKVERSIONEXPIRED:
                Toast.makeText(LoginActivity.this, "一登SDK版本过低", Toast.LENGTH_SHORT).show();
                Cache.clearCached(LoginActivity.this);//清楚所有数据
                break;
            default:
                Cache.clearCached(LoginActivity.this);//清楚所有数据
                break;
        }


    }
}
