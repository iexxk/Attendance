package com.xuan.attendance;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.Bmob;


/**
 * Created by xuan on 2016/5/23.
 */
public class mApplication extends Application {

    /**
     * bmob的key
     * */
     private String APPID = "d7fbde96e436c45f1e5642c86696c33d";
    @Override
    public void onCreate() {
        super.onCreate();


        //-----------------------------bmob后台初始化
        //提供以下两种方式进行初始化操作：
//		//第一：设置BmobConfig，允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)
//		BmobConfig config =new BmobConfig.Builder(this)
//		//设置appkey
//		.setApplicationId(APPID)
//		//请求超时时间（单位为秒）：默认15s
//		.setConnectTimeout(30)
//		//文件分片上传时每片的大小（单位字节），默认512*1024
//		.setUploadBlockSize(1024*1024)
//		//文件的过期时间(单位为秒)：默认1800s
//		.setFileExpiration(5500)
//		.build();
//		Bmob.initialize(config);
        //第二：默认初始化
        Bmob.initialize(this, APPID);




    }
    //运用list来保存们每一个activity是关键
    private List<Activity> mList = new LinkedList<Activity>();
    //为了实现每次使用该类时不创建新的对象而创建的静态对象
    private static mApplication instance;
    //构造方法
    public mApplication(){

    }
    //实例化一次
    public synchronized static mApplication getInstance(){
        if (null == instance) {
            instance = new mApplication();
        }
        return instance;
    }
    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }
    //关闭每一个list内的activity
    public void exit() {
        try {
            for (Activity activity:mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
    //杀进程
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }


}