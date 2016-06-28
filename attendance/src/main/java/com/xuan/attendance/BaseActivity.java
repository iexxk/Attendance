package com.xuan.attendance;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/1/15.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication.getInstance().addActivity(this);
    }

    public void toast(String info) {
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }
    public void log(String info){
        Log.e(getClass().getName().substring(getClass().getName().lastIndexOf(".")+1),info);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.back_in_left, R.anim.back_out_right);
    }

    /**
     * 进入Activity动画
     */
    public void EnterActivityAnim(){
        overridePendingTransition(R.anim.in_right, R.anim.out_left);
    }

    /**
     * 退出Acitvity动画
     */
    public void ExitActivityAnim(){
        overridePendingTransition(R.anim.in_right, R.anim.out_left);
    }
}
