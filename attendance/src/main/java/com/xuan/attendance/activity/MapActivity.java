package com.xuan.attendance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.xuan.attendance.R;
import com.xuan.attendance.function.Location;

public class MapActivity extends AppCompatActivity implements View.OnClickListener,LocationSource,AMap.OnCameraChangeListener {
    MapView mapView;
    ImageView back;
    ImageView center;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private CameraPosition cameraPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initview();
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.setLocationSource(this);// 设置定位按钮监听
        aMap.setOnCameraChangeListener(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true); // 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true); // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        setResult(2412);  //未设置位置
    }

    private void initview() {
        //获取地图控件引用
        mapView = (MapView) findViewById(R.id.map);
        back = (ImageView) findViewById(R.id.all_back_img);
        center = (ImageView) findViewById(R.id.mapshow_mapscenter_img);
        back.setOnClickListener(this);
        center.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.all_back_img:
                finish();
                break;
            case R.id.mapshow_mapscenter_img:
                Intent intent = new Intent();
                intent.putExtra("location",cameraPosition);
                setResult(2413,intent);
                finish();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        Log.e("xuan", "定位按钮点击了");
        Location location = new Location(MapActivity.this);
        location.setlocationlist(new Location.LocationCallback() {
            @Override
            public void onLocationlist(AMapLocation aMapLocation) {
//                Toast.makeText(MapActivity.this, mapUtils.getLocationStr(aMapLocation), Toast.LENGTH_SHORT).show();
                if (mListener != null && aMapLocation != null) {
                    if (aMapLocation != null
                            && aMapLocation.getErrorCode() == 0) {
                        mListener.onLocationChanged(aMapLocation);// 显示系统小蓝
                        aMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                    } else {
                        String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                        Log.e("AmapErr", errText);
                    }
                }
            }
        });
        location.start();
    }

    @Override
    public void deactivate() {
        mListener = null;
        Log.e("xuan", "停止定位");
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
      this.cameraPosition=cameraPosition;
    }
}
