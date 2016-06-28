package com.xuan.attendance.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.isnc.facesdk.SuperID;
import com.xuan.attendance.MainActivity;
import com.xuan.attendance.R;

import cn.bmob.v3.BmobUser;

public class Aty_Welcome extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.aty_welcome, null);

		setContentView(view);
		SuperID.initFaceSDK(this);
		SuperID.setDebugMode(true);
		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(1500);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
//				if (Cache.getCached(Aty_Welcome.this, SDKConfig.KEY_ACCESSTOKEN).equals("")) {
//					startActivity(new Intent(Aty_Welcome.this, LoginActivity.class));
//				}else {
//					startActivity(new Intent(Aty_Welcome.this, MainActivity.class));
//				}
				if (BmobUser.getCurrentUser(Aty_Welcome.this)!=null){
					startActivity(new Intent(Aty_Welcome.this, MainActivity.class));
				}else {
					startActivity(new Intent(Aty_Welcome.this, LoginActivity.class));
				}
				finish();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

		});
	}
}
