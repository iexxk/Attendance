package com.xuan.attendance.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xuan on 2016/6/11.
 */
public class TimeUtil {
    /**
     * bjTime ,据测试不准
     */
    public static final   String bjTimeURL = "http://www.bjtime.cn";
    /**
     * 百度
     */
    public static final   String baiduURL = "http://www.baidu.com";
    /**
     * 淘宝
     */
    public static final   String taobaoURL = "http://www.taobao.com";
    /**
     * 中国科学院国家授时中心
     */
    public static final  String ntscURL = "http://www.ntsc.ac.cn";
    /**
     * 360安全卫士
     */
    public static final   String safe360URL = "http://www.360.cn";
    /**
     * 北京时间
     */
    public static final   String beijingURL = "http://www.beijing-time.org";


    /***
     *  获取网络时间
     * @param webUrl
     * @return yyyy-MM-dd HH:mm:ss string
     */
    public static String getWebsiteDatetime_str(String webUrl){
        try {
            URL url = new URL(webUrl);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld);// 转换为标准时间对象
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);// 输出北京时间
            return sdf.format(date);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     *  获取网络时间
     * @param webUrl
     * @return date
     */
    public static Date getWebsiteDatetime(String webUrl){
        try {
            URL url = new URL(webUrl);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld);// 转换为标准时间对象
            return date;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 计算时间差
     * @param starTime
     * @param endTime
     * @return
     */
    public static String getTimeDifference_str(String starTime, String endTime) {
        String timeString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endTime);

            long diff = parse1.getTime() - parse.getTime();
            if (diff <= 0) {
               diff=diff+24*60*60*1000;
            } else {

            }
            long hour1 = diff / (60 * 60 * 1000);
            long min1 = ((diff / (60 * 1000)) - hour1 * 60);
            timeString = hour1 + "小时" + min1 + "分";


        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return timeString;

    }

    /***
     * 计算时间差
     * @param starTime
     * @param endTime
     * @return
     */
    public static long getTimeDifference(String starTime, String endTime) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endTime);

            long diff = parse1.getTime() - parse.getTime();
            if (diff <= 0) {
                diff=diff+24*60*60*1000;
            } else {

            }

return diff;

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;

    }

}
