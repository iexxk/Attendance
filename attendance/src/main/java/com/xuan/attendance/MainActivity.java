package com.xuan.attendance;

import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.google.gson.Gson;
import com.isnc.facesdk.SuperID;
import com.isnc.facesdk.common.Cache;
import com.isnc.facesdk.common.SDKConfig;
import com.isnc.facesdk.common.SuperIDUtils;
import com.xuan.attendance.activity.AttendanceListActivity;
import com.xuan.attendance.activity.EventListActivity;
import com.xuan.attendance.activity.MapActivity;
import com.xuan.attendance.bean.Attendance;
import com.xuan.attendance.bean.Event;
import com.xuan.attendance.bean.MyUser;
import com.xuan.attendance.bean.SuperOneLogin;
import com.xuan.attendance.function.BmobDataMsg;
import com.xuan.attendance.function.Location;
import com.xuan.attendance.util.DialogView;
import com.xuan.attendance.util.mapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, LocationSource,AMap.OnInfoWindowClickListener {

    final int LOCATIONSET = 2412;
    ImageView headimage;
    TextView useremail;
    MapView mapView;
    MenuItem authorization_item;
    EditText ed_location;
    CameraPosition cameraPosition;
    private AMap aMap;
    private OnLocationChangedListener mListener;

    private  List<Marker> markers=new ArrayList<>();
    private Attendance mAttendance;

    // 用于接收地理围栏提醒的pendingIntent
    private PendingIntent mPendingIntent = null;
    public static final String GEOFENCE_BROADCAST_ACTION = "com.xuan.attendance";
    Location mlocation=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View nav_header_main = navigationView.getHeaderView(0);
//      View nav_header_main=navigationView.inflateHeaderView(R.layout.nav_header_main);    //添加并获得view(布局里就不需要添加了不然会成2个)
        headimage = (ImageView) nav_header_main.findViewById(R.id.imageView);
        headimage.setOnClickListener(this);

        useremail = (TextView) nav_header_main.findViewById(R.id.textView);
        useremail.setText("" + (String) BmobUser.getObjectByKey(MainActivity.this, "email"));
        updateHeadimg(headimage);

        Menu menuNav = navigationView.getMenu();
        authorization_item = menuNav.findItem(R.id.nav_camera);

        if (((String) BmobUser.getObjectByKey(MainActivity.this, "superidUid")).equals("false")) {
            Toast.makeText(MainActivity.this, "请先点击菜单里的的“点击绑定”绑定人脸", Toast.LENGTH_LONG).show();
        } else {
            authorization_item.setTitle("解除绑定");
        }

        //获取地图控件引用
        mapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.setLocationSource(this);// 设置定位按钮监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true); // 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true); // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setOnInfoWindowClickListener(this);


//注册Receiver，设置过滤器
        IntentFilter fliter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        fliter.addAction(GEOFENCE_BROADCAST_ACTION);
//mGeoFenceReceiver为自定义的广播接收器
        registerReceiver(mGeoFenceReceiver, fliter);
//声明对应的intent对象
        Intent intent = new Intent(GEOFENCE_BROADCAST_ACTION);
        mPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0,intent, 0);
        mlocation=new Location(this);

        mlocation.setlocationlist(new Location.LocationCallback() {
            @Override
            public void onLocationlist(AMapLocation aMapLocation) {
                Log.e("xuan",mapUtils.getLocationStr(aMapLocation));
            }
        });

        initeventmap();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // 开发者应用自己用户的信息
    String userinfo = SuperID.formatInfo(this, "xuan", "asd");


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {

            if (!((String) BmobUser.getObjectByKey(MainActivity.this, "superidUid")).equals("false")) {
                // 解除绑定
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("解除将不能用人脸登录和签到功能，是否解除与一登账号的绑定？").setCancelable(false)
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 解绑一登
                                SuperID.userCancelAuthorization(MainActivity.this, new SuperID.IntSuccessCallback() {
                                    @Override
                                    public void onSuccess(int arg0) {
                                        Cache.clearCached(MainActivity.this);//清楚所有数据
                                        item.setTitle("点击绑定");
                                        headimage.setImageDrawable(getResources().getDrawable(R.drawable.userhead));
                                        BmobUser bmobUser = BmobUser.getCurrentUser(MainActivity.this);
                                        MyUser myUser = new MyUser();
                                        myUser.setSuperidUid("false");
                                        myUser.update(MainActivity.this, bmobUser.getObjectId(), new UpdateListener() {
                                            @Override
                                            public void onSuccess() {
                                                // TODO Auto-generated method stub
                                                Log.i("xuan", "更新用户信息成功:");
                                            }

                                            @Override
                                            public void onFailure(int code, String msg) {
                                                // TODO Auto-generated method stub
                                                Log.i("xuan", "更新用户信息失败:" + msg);
                                            }
                                        });
                                        //取消对bomob的授权
                                        BmobUser.dissociateAuthData(MainActivity.this, BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ, new UpdateListener() {

                                            @Override
                                            public void onSuccess() {
                                                // TODO Auto-generated method stub
                                                Toast.makeText(MainActivity.this, "取消一登人脸关联成功", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onFailure(int code, String msg) {
                                                // TODO Auto-generated method stub
                                                if (code == 208) {// 208错误指的是没有绑定相应账户的授权信息
                                                    Toast.makeText(MainActivity.this, "你没有关联该账号", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(MainActivity.this, "取消一登人脸关联失败：code =" + code + ",msg = " + msg, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                }, new SuperID.IntFailCallback() {

                                    @Override
                                    public void onFail(int error) {
                                        Toast.makeText(MainActivity.this, "解绑失败", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).show();

            } else {
                //绑定一登账号 FaceBundle(Activity activity, String uid, String userinfo)
                SuperID.faceBundle(MainActivity.this, String.valueOf(System.currentTimeMillis()), userinfo);
            }

        } else if (id == R.id.nav_gallery) {  //签到列表

            startActivity(new Intent(MainActivity.this, AttendanceListActivity.class).putExtra("eventID","null"));

        } else if (id == R.id.nav_slideshow) {  //创建活动
            View event_dialog = View.inflate(this, R.layout.event_dialog, null);
            final EditText ed_time = (EditText) event_dialog.findViewById(R.id.ed_time);
            final EditText ed_name= (EditText) event_dialog.findViewById(R.id.ed_name);
            final EditText ed_msg= (EditText) event_dialog.findViewById(R.id.ed_msg);
            ed_location = (EditText) event_dialog.findViewById(R.id.ed_location);
            final Spinner sp_timerange= (Spinner) event_dialog.findViewById(R.id.sp_timerange);
            final Spinner sp_loacationrange= (Spinner) event_dialog.findViewById(R.id.sp_locationrangr);

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
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
                    BmobDataMsg bmobDataMsg=new BmobDataMsg(MainActivity.this);
                    bmobDataMsg.uploadEvent(ed_name,ed_msg,ed_time,sp_timerange,ed_location,sp_loacationrange,cameraPosition);
                }
            });
            builder.create().show();
            ed_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
                            ed_time.setText("" + i + ":" + i1);
                        }
                    }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);
                    timePickerDialog.show();
                }
            });
            ed_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(MainActivity.this, MapActivity.class), LOCATIONSET);
                }
            });

        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(MainActivity.this, EventListActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("确认要退出并注销" + (String) BmobUser.getObjectByKey(MainActivity.this, "email"));
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Cache.clearCached(MainActivity.this);//清楚所有数据
                        BmobUser.logOut(MainActivity.this);
                        mApplication.getInstance().exit();
                        System.exit(0);
                    }
                });
                builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create();
                builder.show();

                break;

            default:
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
        Location location = new Location(MainActivity.this);
        location.setlocationlist(new Location.LocationCallback() {
            @Override
            public void onLocationlist(AMapLocation aMapLocation) {
             //   Toast.makeText(MainActivity.this, mapUtils.getLocationStr(aMapLocation), Toast.LENGTH_SHORT).show();
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

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        Log.e("xuan", "停止定位");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("xuan", "结果代码：" + resultCode);
        String appinfo = Cache.getCached(MainActivity.this, SDKConfig.KEY_APPINFO);
        Log.e("xuan", "结果："+appinfo);
        if (!appinfo.equals("")) {
            Gson gson=new Gson();
            SuperOneLogin superOneLogin=  gson.fromJson(appinfo, SuperOneLogin.class);

            switch (resultCode) {
                // 授权成功
                case SDKConfig.AUTH_SUCCESS:
                    updateHeadimg(headimage);
                    // 授权成功后 获取一登用户信息
                    String uid = Cache.getCached(MainActivity.this, SDKConfig.KEY_UID);
                    useremail.setText(superOneLogin.getPhone());
                    BmobUser bmobUser = BmobUser.getCurrentUser(MainActivity.this);
                    //授权
                    BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ, "rMG9QWXGQ5sH0axG5GA45RT2qaaRhfY", "20160601", uid);
                    bmobUser.associateWithAuthData(MainActivity.this, authInfo, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            Log.i("smile", "第三方授权成功：");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.i("smile", "第三方授权失败：" + s);
                        }
                    });


                    //注册uid
                    MyUser myUser = new MyUser();
                    myUser.setSuperidUid(uid);
                    myUser.update(MainActivity.this, bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            // TODO Auto-generated method stub
                            Log.i("xuan", "更新用户信息成功:");
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            // TODO Auto-generated method stub
                            Log.i("xuan", "更新用户信息失败:" + msg);
                        }
                    });

                    authorization_item.setTitle("解除绑定");
                    break;
                // 登录成功
                case SDKConfig.LOGINSUCCESS:
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("绑定失败!");
                    builder.setMessage("该脸已经被" + "xx" + "绑定了,请解除绑定，是否退出");
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            BmobUser.logOut(MainActivity.this);
                            Cache.clearCached(MainActivity.this);//清楚所有数据
                            mApplication.getInstance().exit();
                            System.exit(0);
                        }
                    });
                    builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Cache.clearCached(MainActivity.this);//清楚所有数据
                        }
                    });
                    builder.create();
                    builder.show();
                    break;
                case SDKConfig.VERIFY_SUCCESS:   //签到成功
                    mAttendance.setHeadimg(superOneLogin.getAvatar());
                    BmobDataMsg bmobDataMsg = new BmobDataMsg(MainActivity.this);
                    bmobDataMsg.uploadAttendce(mAttendance);
                    break;
                case SDKConfig.VERIFY_FAIL:
                    Toast.makeText(MainActivity.this, "签到失败！", Toast.LENGTH_SHORT).show();
                    break;
                case 2412:  //返回2412未设置位置
//
                    break;
                case 2413:  //设置了位置
                    cameraPosition = (CameraPosition) data.getExtras().get("location");
                    Log.e("xuan", "22222222" + cameraPosition.toString());
                    GeocodeSearch geocodeSearch = new GeocodeSearch(MainActivity.this);
                    geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                        @Override
                        public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
                            if (rCode == 1000) {
                                if (result != null && result.getRegeocodeAddress() != null
                                        && result.getRegeocodeAddress().getFormatAddress() != null) {
                                    ed_location.setText(result.getRegeocodeAddress().getFormatAddress()
                                            + "附近");
//                                if (  result.getRegeocodeAddress().getAois().size()!=0)
//                                    Log.e("xuan",  "as"+ result.getRegeocodeAddress().getAois().get(0).getAoiName());
                                } else {
                                    Log.e("xuan", "没有结果");
                                }
                            } else {
                                Log.e("xuan", "错位代码：" + rCode);
                            }
                        }

                        @Override
                        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                        }
                    });
                    RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude), 200, GeocodeSearch.AMAP);
                    geocodeSearch.getFromLocationAsyn(query);
                    break;
                default:
                    AlertDialog.Builder errbuilder = new AlertDialog.Builder(MainActivity.this);
                    errbuilder.setMessage("登录异常(过期)应用将退出，请重新登录" + (String) BmobUser.getObjectByKey(MainActivity.this, "email"));
                    errbuilder.setCancelable(false);
                    errbuilder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Cache.clearCached(MainActivity.this);//清楚所有数据
                            BmobUser.logOut(MainActivity.this);
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

    /***
     * 更新头像
     *
     * @param headimage
     */
    private void updateHeadimg(ImageView headimage) {
        File file = new File(SDKConfig.TEMP_PATH + "/" + Cache.getCached(MainActivity.this, SDKConfig.KEY_PHONENUM) + ".JPEG");
        if (file != null && file.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(SDKConfig.TEMP_PATH + "/"
                    + Cache.getCached(MainActivity.this, SDKConfig.KEY_PHONENUM) + ".JPEG");
            headimage.setImageBitmap(SuperIDUtils.getRoundedCornerBitmap(bm, 480));
        }
    }

    void initeventmap(){
        final Dialog dialog = DialogView.createLoadingDialog(this,"加载数据中...");
        dialog.show();
        BmobQuery<Event> eventBmobQuery=new BmobQuery<>();
        eventBmobQuery.setLimit(50);
        eventBmobQuery.findObjects(this, new FindListener<Event>() {
            @Override
            public void onSuccess(List<Event> list) {
                for(int i = 0; i < list.size(); i++)
                {
                    Event event= list.get(i);
                    MarkerOptions markerOptions=new MarkerOptions().anchor(0.5f,0.5f).position(event.getLocation()).title(event.getName()).snippet("点击签到");
                    Marker marker=new Marker(markerOptions);

                    aMap.addMarker(markerOptions).setObject(event);
                    markers.add(marker);

                    aMap.addCircle(new CircleOptions().center(event.getLocation()).radius(event.getLocationrangr()).strokeColor(Color.argb(30, 1, 1, 1))
                            .fillColor(Color.argb(30, 1, 1, 1)).strokeWidth(5));
                    // 添加地理围栏，
                    // 第一个参数：围栏ID,可以自定义ID,示例中为了方便只使用一个ID;第二个：纬度；第三个：精度；
                    // 第四个：半径；第五个：过期时间，单位毫秒，-1代表不过期；第六个：接收触发消息的PendingIntent


                    mlocation.getLocationClient().addGeoFenceAlert(""+i,
                            event.getLocation().latitude, event.getLocation().longitude,
                            event.getLocationrangr(), -1, mPendingIntent);
                }


                // 设置定位参数
                mlocation.setOnece(false);
                // 启动定位,地理围栏依赖于持续定位
                mlocation.start();

                dialog.dismiss();
            }

            @Override
            public void onError(int i, String s) {
                dialog.dismiss();
                Log.e("xuan","mmmmmmmmmm"+s);
            }
        });
    }
    private BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("xuan","jisss"+intent.getAction());
            // 接收广播
            if (intent.getAction().equals(GEOFENCE_BROADCAST_ACTION)) {
                Bundle bundle = intent.getExtras();
                // 根据广播的event来确定是在区域内还是在区域外
                int i = intent.getIntExtra("status", -1);
                int status = bundle.getInt("event");
                String geoFenceId = bundle.getString("fenceid");
                Log.e("xuan", geoFenceId+"onReceive: "+status+"aaaa"+i);
                if (status == 1) {



                    Log.e("xuan","进入围栏区域");
                    Toast.makeText(MainActivity.this,"你可以签到了",Toast.LENGTH_SHORT).show();
                    // 进入围栏区域
                    // 可以自定义提醒方式,示例中使用的是文字方式
//                    if (cbAlertIn.isChecked()) {
//                        mHandler.sendEmptyMessage(1);
//                    }
                } else if (status == 2) {
                    Log.e("xuan","离开围栏区域");
                    Toast.makeText(MainActivity.this,"没有可签到的活动",Toast.LENGTH_SHORT).show();
                    // 离开围栏区域
                    // 可以自定义提醒方式,示例中使用的是文字方式
//                    if (cbAlertOut.isChecked()) {
//                        mHandler.sendEmptyMessage(2);
//                    }
                }
            }
        }
    };

    @Override
    public void onInfoWindowClick(final Marker marker) {

        mAttendance =new Attendance();
        BmobDataMsg bmobDataMsg=new BmobDataMsg(MainActivity.this);
        bmobDataMsg.attendance_proess((Event)marker.getObject(),mAttendance);

    }

}
