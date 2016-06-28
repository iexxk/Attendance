package com.xuan.attendance.util;

import android.os.CountDownTimer;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xuan on 2016/6/11.
 */
public class MyCountDownTimer extends CountDownTimer {

    private EditText countdownButton;
    private String buttonText;
    private String time;
    private Integer timerange;


    /**
     *
     * @param countDownInterval    间隔时间（毫秒）
     * @param button               倒计时的按钮
     * @param buttonText           倒计时按钮的初始文字
     */
    public MyCountDownTimer(long millisInFuture,String time, Integer timerange, long countDownInterval, EditText button, String buttonText) {
        this( TimeUtil.getTimeDifference(new SimpleDateFormat("HH:mm", Locale.CHINA).format(new Date(millisInFuture)),time), countDownInterval);
        this.timerange=timerange;
        this.time=time;
        this.countdownButton = button;
        this.buttonText = buttonText;

    }


    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        long hour1 = millisUntilFinished / (60 * 60 * 1000);
        long min1 = ((millisUntilFinished / (60 * 1000)) - hour1 * 60);
        long s = (millisUntilFinished / 1000  - hour1 * 60 * 60 - min1 * 60);
        String  timeString = hour1 + "小时" + min1 + "分"+s+ "秒";
        countdownButton.setEnabled(false);
        countdownButton.setText("等待" +timeString);
    }

    @Override
    public void onFinish() {
        countdownButton.setEnabled(true);
        countdownButton.setText(buttonText);
    }
}
