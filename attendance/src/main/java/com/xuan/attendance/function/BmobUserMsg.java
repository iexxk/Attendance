package com.xuan.attendance.function;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.isnc.facesdk.SuperID;
import com.isnc.facesdk.soloader.SoDownloadManager;
import com.xuan.attendance.MainActivity;
import com.xuan.attendance.R;
import com.xuan.attendance.bean.MyUser;
import com.xuan.attendance.util.DialogView;
import com.xuan.attendance.util.LogUtils;
import com.xuan.attendance.util.Tools;

import org.json.JSONObject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.EmailVerifyListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by xuan on 2016/5/25.
 */
public class BmobUserMsg {

    private Context context;
    private Activity activity;
    private Intent intent = new Intent();


    public BmobUserMsg(Context context) {
        this.context = context;
        activity = (Activity) context;

    }

    /**
     * 用户登录
     *
     * @param mEmail_login    电子邮件
     * @param mPassword_login 密码
     */
    public void Login(EditText mEmail_login, EditText mPassword_login) {
        String email = mEmail_login.getText().toString().trim();
        String passwd = mPassword_login.getText().toString().trim();
        final StringBuffer s=new StringBuffer("");
        if (TextUtils.isEmpty(email)){
            s.append(context.getString(R.string.email_no_null));
        }
        if (TextUtils.isEmpty(passwd)){
            s.append(context.getString(R.string.pwd_no_null));
        }
        if (!TextUtils.isEmpty(s.toString())){
            Toast.makeText(context, s.toString(), Toast.LENGTH_SHORT).show();
            return;
        }
        final Dialog dialog = DialogView.createLoadingDialog(context, context.getString(R.string.loginrun));
        dialog.show();


        BmobUser.loginByAccount(context, email, passwd, new LogInListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if (e != null) {
                    Log.e("TAG", e.toString()+e.getErrorCode());
                    Toast.makeText(context,context.getString(R.string.usernameorpassworderror), Toast.LENGTH_SHORT).show();
                } else {
                    LogUtils.e(LogUtils.getLineInfo(),"done");
                    intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    activity.finish();
                }
                dialog.dismiss();
            }
        });

    }

    /**
     * 用户登录
     *
     * @param uid   授权id
     */
    public void Login(String uid) {
        final Dialog dialog = DialogView.createLoadingDialog(context, context.getString(R.string.loginrun));
        dialog.show();
        BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ,"rMG9QWXGQ5sH0axG5GA45RT2qaaRhfY", "20160601",uid);
        BmobUser.loginWithAuthData(context, authInfo, new OtherLoginListener() {

            @Override
            public void onSuccess(JSONObject userAuth) {
                // TODO Auto-generated method stub
                intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                activity.finish();
                dialog.dismiss();
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(context,"第三方登陆失败："+msg, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

        });

    }

    /**
     * 注册
     *
     * @param mEmail_signup
     * @param mPassword_signup
     * @param mPassword_signup_again
     */
    public void SignUp(EditText mEmail_signup, EditText mPassword_signup, EditText mPassword_signup_again) {
        final String email = mEmail_signup.getText().toString().trim();
        final String passwd = mPassword_signup.getText().toString().trim();
        String passwd_again = mPassword_signup_again.getText().toString().trim();
        StringBuffer s = new StringBuffer("");
        if (TextUtils.isEmpty(email)) {
            s.append(context.getString(R.string.email_no_null));
        }
        if (TextUtils.isEmpty(passwd)) {
            s.append(context.getString(R.string.pwd_no_null));
        }
        if (TextUtils.isEmpty(passwd_again)) {
            s.append(context.getString(R.string.pwd_again_no_null));
        }
        LogUtils.e(LogUtils.getLineInfo(), "SignUp:" + s.toString());
        if (!TextUtils.isEmpty(s.toString())) {
            Toast.makeText(context, s.toString(), Toast.LENGTH_SHORT).show();
            return;
        }
        final Dialog signdialog = DialogView.createLoadingDialog(context, context.getString(R.string.signrun));
        signdialog.show();

        final MyUser myUser = new MyUser();
        myUser.setUsername(email);
        myUser.setPassword(passwd);
        myUser.setEmail(email);
        myUser.setSuperidUid("false");
        myUser.signUp(context, new SaveListener() {
            @Override
            public void onSuccess() {
                BmobUser.requestEmailVerify(context, email, new EmailVerifyListener() {
                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context,"请求验证邮件成功，请到" + email + "邮箱中进行激活。",Toast.LENGTH_SHORT).show();

                        BmobUser.loginByAccount(context, email, passwd, new LogInListener<MyUser>() {
                            @Override
                            public void done(MyUser myUser, BmobException e) {
                                if (e != null) {
                                    Log.e("TAG", e.toString()+e.getErrorCode());
                                    Toast.makeText(context,context.getString(R.string.usernameorpassworderror), Toast.LENGTH_SHORT).show();
                                } else {
                                    LogUtils.e(LogUtils.getLineInfo(),"done");
                                    intent = new Intent(context, MainActivity.class);
                                    context.startActivity(intent);
                                    activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                    activity.finish();
                                }
                                signdialog.dismiss();
                            }
                        });

                    }
                    @Override
                    public void onFailure(int code, String e) {
                        // TODO Auto-generated method stub
                        Toast.makeText(context,"请求验证邮件失败:" + e,Toast.LENGTH_SHORT).show();
                        signdialog.dismiss();
                    }
                });

            }

            @Override
            public void onFailure(int code, String msg) {
                if(msg==null){
                    Log.e("TAG", msg+"errcode:"+code);
                    signdialog.dismiss();
                }else{
                    if (msg.equals("username " + email + " already taken"))
                        Toast.makeText(context, context.getString(R.string.username) + email + context.getString(R.string.already_taken), Toast.LENGTH_SHORT).show();
                    signdialog.dismiss();
                }
            }
        });
    }

    public void restpwd(EditText mEmail_forget) {
        String emial = mEmail_forget.getText().toString().trim();
        if (TextUtils.isEmpty(emial)) {
            Toast.makeText(context, context.getString(R.string.please_i_e_a), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Tools.isEmail(emial)) {
            Toast.makeText(context, context.getString(R.string.email_error), Toast.LENGTH_SHORT).show();
            return;
        }
        final Dialog resetDialog = DialogView.createLoadingDialog(context, context.getString(R.string.sendemailrun));
        resetDialog.show();

        BmobUser.resetPasswordByEmail(context, emial, new ResetPasswordByEmailListener() {
            @Override
            public void onSuccess() {
                activity.finish();
                Toast.makeText(context, context.getString(R.string.email_get_password_send_success), Toast.LENGTH_SHORT).show();
                resetDialog.dismiss();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(context, context.getString(R.string.please_once_a), Toast.LENGTH_SHORT).show();
                resetDialog.dismiss();
            }
        });

    }

    /***
     *人脸登录
     */
    public void SuperidLogin() {
        //sdk无加载so库 调用示例
        SuperID.faceLogin(activity, new SuperID.SoLoaderCallback() {
            @Override
            public void soLoader() {
                doDownload();
            }
        });
    }
    private void doDownload() {
        final ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("刷脸升级包下载");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        SoDownloadManager.checkSoAndDownload(activity, new SoDownloadManager.ProgressCallback() {
            @Override
            public void getProgress(int progress) {
                //progress为下载进度0~100
                mProgressDialog.setProgress(progress);
                if (progress == 100) {
                    //加载成功
                    mProgressDialog.dismiss();
                    SuperID.faceLogin(activity);
                } else if (progress == -100) {
                    //加载失败
                    mProgressDialog.dismiss();
                }
            }
        });
    }
}
