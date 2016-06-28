package com.xuan.attendance.bean;

import com.amap.api.maps2d.model.LatLng;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by xuan on 2016/6/1.
 */
public class Attendance extends BmobObject{
    private Event event;
    private BmobUser user;
    private String msg;
    private String name;
    private BmobGeoPoint location;
    private String headimg;

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public BmobUser getUser() {
        return user;
    }

    public void setUser(BmobUser user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LatLng getLocation() {
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        return latLng;
    }

    public void setLocation( LatLng latLng) {
        this.location=new BmobGeoPoint(latLng.longitude,latLng.latitude);
    }



}
