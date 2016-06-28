package com.xuan.attendance.function;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xuan.attendance.R;
import com.xuan.attendance.util.DialogView;
import com.xuan.attendance.util.mapUtils;

/**
 * Created by xuan on 2016/5/29.
 */
public class Location implements AMapLocationListener{
    /*
 * 回调接口
 */
    public interface LocationCallback {
        public void onLocationlist(AMapLocation aMapLocation);
    }
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private   Dialog locationdialog;
    private  Context context;
    private LocationCallback mLocationCallback=null;
    private boolean onece=true;

    public void setOnece(boolean onece) {
        this.onece = onece;
    }

    public AMapLocationClient getLocationClient() {
        return locationClient;
    }

    public void setlocationlist(LocationCallback mLocationCallback){
        this.mLocationCallback=mLocationCallback;
    }

    public Location(Context context) {
        this.context=context;
        locationClient = new AMapLocationClient(context.getApplicationContext());

    }

    /***
     * 启动定位
     */
    public void start(){
        initOption();
        // 设置定位监听
        locationClient.setLocationListener(this);
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
        locationdialog = DialogView.createLoadingDialog(context, context.getString(R.string.locationing));
        locationdialog.show();
    }
    // 根据控件的选择，重新设置定位参数
    private void initOption() {
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //只有持续定位设置定位间隔才有效，单次定位无效
        locationOption.setOnceLocation(onece);
     // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
        locationOption.setInterval(Long.valueOf(10000));
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(false);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        String result = mapUtils.getLocationStr(aMapLocation);
        Log.e("xuan","123"+result);
        if(mLocationCallback!=null)
        {mLocationCallback.onLocationlist(aMapLocation);}
        locationdialog.dismiss();
    }
}
