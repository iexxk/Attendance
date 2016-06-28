package com.xuan.attendance.bean;

import com.amap.api.maps2d.model.LatLng;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by xuan on 2016/6/1.
 */
public class Event extends BmobObject {
    private BmobGeoPoint location;
    private Integer locationrangr;
    private String name;
    private String time;
    private Integer timerange;
    private BmobUser user;
    private String msg;
    private String location_str;
    private String user_name;
    private Integer number;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation_str() {
        return location_str;
    }

    public void setLocation_str(String location_str) {
        this.location_str = location_str;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public LatLng getLocation() {
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        return latLng;
    }

    public void setLocation( LatLng latLng) {
         this.location=new BmobGeoPoint(latLng.longitude,latLng.latitude);
    }

    public Integer getLocationrangr() {
        return locationrangr;
    }

    public void setLocationrangr(Integer locationrangr) {
        this.locationrangr = locationrangr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTimerange() {
        return timerange;
    }

    public void setTimerange(Integer timerange) {
        this.timerange = timerange;
    }

    public BmobUser getUser() {
        return user;
    }

    public void setUser(BmobUser user) {
        this.user = user;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
