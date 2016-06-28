package com.xuan.attendance.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xuan.attendance.R;
import com.xuan.attendance.adapter.AttendanceAdapter;
import com.xuan.attendance.bean.Attendance;
import com.xuan.attendance.util.DialogView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class AttendanceListActivity extends AppCompatActivity {
    private AttendanceAdapter mAttendanceAdapter;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        String eventId= (String) getIntent().getExtras().getCharSequence("eventID");

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
        BmobQuery<Attendance> attendBmobQuery=new BmobQuery<>();
        attendBmobQuery.include("event,user");
        if (!eventId.equals("null")){
            attendBmobQuery.addWhereEqualTo("event",eventId);
        }
        attendBmobQuery.setLimit(50);
        attendBmobQuery.findObjects(this, new FindListener<Attendance>() {
            @Override
            public void onSuccess(List<Attendance> list) {
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

    private void initAdapter(List<Attendance> list) {
        mAttendanceAdapter = new AttendanceAdapter(this,list);
        mAttendanceAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT); //动画
        mAttendanceAdapter.isFirstOnly(false);  //每次都有动画
        mAttendanceAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String content = null;
                Attendance attendance = (Attendance) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.img_head:
                        content = "img:" + attendance.getMsg();
                        break;
                    case R.id.txt_name:
                        content = "name:" + attendance.getName();
                        break;
                }
                Toast.makeText(AttendanceListActivity.this, content, Toast.LENGTH_LONG).show();
            }
        });
        mRecyclerView.setAdapter(mAttendanceAdapter);
    }
}
