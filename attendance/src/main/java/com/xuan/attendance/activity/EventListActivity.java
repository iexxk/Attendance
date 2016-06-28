package com.xuan.attendance.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.isnc.facesdk.common.Cache;
import com.isnc.facesdk.common.SDKConfig;
import com.xuan.attendance.R;
import com.xuan.attendance.adapter.EventAdapter;
import com.xuan.attendance.bean.Attendance;
import com.xuan.attendance.bean.Event;
import com.xuan.attendance.bean.SuperOneLogin;
import com.xuan.attendance.function.BmobDataMsg;
import com.xuan.attendance.mApplication;
import com.xuan.attendance.util.DialogView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

public class EventListActivity extends AppCompatActivity {
    private EventAdapter mEventAdapter;
    private RecyclerView mRecyclerView;
    private Attendance mAttendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ImageView back = (ImageView) findViewById(R.id.all_back_img);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final Dialog dialog = DialogView.createLoadingDialog(this,"加载数据中...");
        dialog.show();
        BmobQuery<Event> eventBmobQuery=new BmobQuery<>();
        eventBmobQuery.setLimit(50);
        eventBmobQuery.findObjects(this, new FindListener<Event>() {
            @Override
            public void onSuccess(List<Event> list) {
                dialog.dismiss();

                initAdapter(list);      //初始化加载列表
            }

            @Override
            public void onError(int i, String s) {
                dialog.dismiss();
                Log.e("xuan","mmmmmmmmmm"+s);
            }
        });


    }

    private void initAdapter(List<Event> list) {
        mEventAdapter = new EventAdapter(this,list);
        mEventAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT); //动画
        mEventAdapter.isFirstOnly(false);  //每次都有动画
        mEventAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String content = null;
                Event event = (Event) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.btn_time_attend:
                        mAttendance =new Attendance();
                        BmobDataMsg bmobDataMsg=new BmobDataMsg(EventListActivity.this);
                        bmobDataMsg.attendance_proess(event,mAttendance);
                        break;
                    case R.id.txt_number:
                        event.getObjectId();
                        Intent intent=new Intent(EventListActivity.this, AttendanceListActivity.class);
                        intent.putExtra("eventID",event.getObjectId());
                        startActivity(intent);

                        break;
                }

            }
        });

        mRecyclerView.setAdapter(mEventAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("xuan", "结果代码：" + resultCode);
        String appinfo = Cache.getCached(EventListActivity.this, SDKConfig.KEY_APPINFO);
        Log.e("xuan", "结果："+appinfo);
        if (!appinfo.equals("")) {
            Gson gson=new Gson();
            SuperOneLogin superOneLogin=  gson.fromJson(appinfo, SuperOneLogin.class);

            switch (resultCode) {

                case SDKConfig.VERIFY_SUCCESS:   //签到成功
                    mAttendance.setHeadimg(superOneLogin.getAvatar());
                    BmobDataMsg bmobDataMsg = new BmobDataMsg(EventListActivity.this);
                    bmobDataMsg.uploadAttendce(mAttendance);
                    break;
                case SDKConfig.VERIFY_FAIL:
                    Toast.makeText(EventListActivity.this, "签到失败！", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    AlertDialog.Builder errbuilder = new AlertDialog.Builder(EventListActivity.this);
                    errbuilder.setMessage("登录异常(过期)应用将退出，请重新登录" + (String) BmobUser.getObjectByKey(EventListActivity.this, "email"));
                    errbuilder.setCancelable(false);
                    errbuilder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Cache.clearCached(EventListActivity.this);//清楚所有数据
                            BmobUser.logOut(EventListActivity.this);
                            mApplication.getInstance().exit();
                            System.exit(0);
                        }
                    });
                    errbuilder.create();
                    errbuilder.show();
                    break;
            }
        }
    }
}
