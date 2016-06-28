package com.xuan.attendance.function;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.isnc.facesdk.SuperID;
import com.xuan.attendance.MainActivity;
import com.xuan.attendance.R;
import com.xuan.attendance.bean.Attendance;
import com.xuan.attendance.bean.Event;
import com.xuan.attendance.util.DialogView;
import com.xuan.attendance.util.MyCountDownTimer;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by xuan on 2016/5/25.
 */
public class BmobDataMsg {

    private Context context;
    private Activity activity;
    private Intent intent = new Intent();

    Integer timer[] = {20, 40, 60};
    Integer locationr[] = {100, 500, 1000, 2000, 5000};

    public BmobDataMsg(Context context) {
        this.context = context;
        activity = (Activity) context;

    }


    public void uploadEvent(EditText name, EditText msg, EditText time, Spinner time_range, EditText location_edi, Spinner location_range, CameraPosition cameraPosition) {
        String name_str = name.getText().toString().trim();
        String msg_str = msg.getText().toString().trim();
        String location_str = location_edi.getText().toString().trim();
        String time_str = time.getText().toString().trim();
        Integer timerange = timer[time_range.getSelectedItemPosition()];
        Integer locationrange = locationr[location_range.getSelectedItemPosition()];

        final StringBuffer s = new StringBuffer("");
        if (TextUtils.isEmpty(name_str)) {
            s.append("活动名称为空");
        }
        if (TextUtils.isEmpty(msg_str)) {
            s.append("活动信息为空");
        }
        if (TextUtils.isEmpty(location_str)) {
            s.append("位置为空");
        }
        if (TextUtils.isEmpty(time_str)) {
            s.append("时间为空");
        }
        if (!TextUtils.isEmpty(s.toString())) {
            Toast.makeText(context, s.toString(), Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("xuan", name_str + msg_str + location_str + time_str + timerange + "," + locationrange);
        final Dialog dialog = DialogView.createLoadingDialog(context, "上传数据中...");
        dialog.show();

        Event event = new Event();
        event.setMsg(msg_str);
        event.setName(name_str);
        event.setNumber(0);
        event.setTime(time_str);
        event.setLocation_str(location_str);
        event.setLocationrangr(locationrange);
        event.setTimerange(timerange);
        event.setUser(BmobUser.getCurrentUser(context));
        event.setUser_name((String) BmobUser.getObjectByKey(context, "username"));
        event.setLocation(cameraPosition.target);
        event.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "创建成功", Toast.LENGTH_SHORT).show();
                activity.finish();
                context.startActivity(new Intent(context, MainActivity.class)); //刷新界面
                dialog.dismiss();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(context, "创建失败", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


    }


    public void uploadAttendce(Attendance attendance) {

        final Dialog dialog = DialogView.createLoadingDialog(context, "上传数据中...");
        dialog.show();
        attendance.getEvent().increment("number", 1);
        attendance.getEvent().update(context);
        attendance.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(context, "签到失败", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


    }

    public void attendance_proess(final Event mEvent, final Attendance mAttendance) {
        // 计算量坐标点距离
        Location location = new Location(context);
        location.setlocationlist(new Location.LocationCallback() {
            @Override
            public void onLocationlist(final AMapLocation aMapLocation) {
                float distance = AMapUtils.calculateLineDistance(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), mEvent.getLocation());

                if (distance <= mEvent.getLocationrangr()) {

                    View event_dialog = View.inflate(context, R.layout.attendance_dialog, null);
                    final EditText ed_time = (EditText) event_dialog.findViewById(R.id.ed_time_attend);
                    final EditText ed_name = (EditText) event_dialog.findViewById(R.id.ed_name_show);
                    final EditText ed_name_attend = (EditText) event_dialog.findViewById(R.id.ed_name_atted);
                    final EditText ed_msg = (EditText) event_dialog.findViewById(R.id.ed_msg_show);
                    final EditText ed_msg_attend = (EditText) event_dialog.findViewById(R.id.ed_msg_attend);

                    ed_time.setText(mEvent.getTime() + "开始，" + mEvent.getTimerange() + "后结束");
                    new MyCountDownTimer(aMapLocation.getTime(), mEvent.getTime(), mEvent.getTimerange(), 1000, ed_time, "验证手机").start();
                    ed_msg.setText(mEvent.getMsg());
                    ed_name.setText(mEvent.getName());


                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                    builder.setView(event_dialog);
                    builder.setCancelable(false);
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String name_attend = ed_name_attend.getText().toString().trim();
                            String msg_attend = ed_msg_attend.getText().toString().trim();
                            final StringBuffer s = new StringBuffer("");
                            if (TextUtils.isEmpty(name_attend)) {
                                s.append("名字不能为空");
                            }
                            if (TextUtils.isEmpty(msg_attend)) {
                                s.append("信息不能为空");
                            }
                            if (!TextUtils.isEmpty(s.toString())) {
                                Toast.makeText(context, s.toString(), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            mAttendance.setName(name_attend);
                            mAttendance.setEvent(mEvent);
                            mAttendance.setMsg(msg_attend);
                            mAttendance.setLocation(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                            mAttendance.setUser(BmobUser.getCurrentUser(context));
                            SuperID.faceVerify(activity, 3);  //刷脸验证
                        }
                    });
                    builder.create().show();

                } else {
                    Toast.makeText(context, "你不在签到范围", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(context, "距离为：" + distance + "距离为" + mEvent.getUser_name(), Toast.LENGTH_SHORT).show();

            }
        });
        location.start();
    }


}
