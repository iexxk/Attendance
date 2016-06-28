package com.xuan.attendance.adapter;


import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xuan.attendance.R;
import com.xuan.attendance.bean.Attendance;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class AttendanceAdapter extends BaseQuickAdapter<Attendance> {

    public AttendanceAdapter(Context context, List<Attendance> list) {
        super(context, R.layout.attendance_item, list);
    }


    @Override
    protected void convert(BaseViewHolder helper, Attendance attendance) {
        helper.setText(R.id.txt_eventname_attend, attendance.getEvent().getName())
                .setText(R.id.txt_msg_attend, attendance.getMsg())
                .setText(R.id.txt_name_attend,attendance.getName())
                .setText(R.id.txt_time_attend, attendance.getUpdatedAt())
                .setImageUrl(R.id.img_head_attend,attendance.getHeadimg())
                .setText(R.id.txt_username_attend,attendance.getUser().getUsername());


    }


}