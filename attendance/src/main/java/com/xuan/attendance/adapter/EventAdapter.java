package com.xuan.attendance.adapter;


import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xuan.attendance.R;
import com.xuan.attendance.bean.Event;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class EventAdapter extends BaseQuickAdapter<Event> {

    public EventAdapter(Context context, List<Event> list) {
        super(context, R.layout.event_item, list);
    }


    @Override
    protected void convert(BaseViewHolder helper, Event event) {
        helper.setText(R.id.txt_name, event.getName())
                .setText(R.id.txt_msg, event.getMsg())
                .setText(R.id.txt_location, event.getLocation_str()+event.getLocationrangr() + "米范围内")
                .setText(R.id.txt_number, "已签到：" + event.getNumber() + "人")
                .setOnClickListener(R.id.txt_number,new OnItemChildClickListener())
                .setText(R.id.txt_time, event.getTime())
                .setText(R.id.txt_timerange, event.getTimerange() + "分钟内")
                .setOnClickListener(R.id.btn_time_attend,new OnItemChildClickListener())
                .setText(R.id.txt_username, event.getUser_name());


    }


}