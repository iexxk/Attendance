package com.xuan.attendance.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/1/14.
 */
public class Tools {


    /**
     * if return ture email is normal else abnormal
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        boolean isMatched = matcher.matches();
        System.out.println(isMatched);
        return isMatched;
    }
    public static boolean isGpsEnabled(Context context) {
        // LocationManager lm = ((LocationManager) context
        // .getSystemService(Context.LOCATION_SERVICE));
        // List<String> accessibleProviders = lm.getProviders(true);
        // return accessibleProviders != null && accessibleProviders.size() > 0;

        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps) {
            return true;
        }

        return false;
    }
    /**
     * 是否打开gps
     *
     * @param context
     */
    public static void openGpsDialog(final Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage("请打开GPS");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                // 转到手机设置界面，用户设置GPS
                Intent intent = new Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent); // 设置完成后返回到原来的界面
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        dialog.show();
    }
    /**     * 按正方形裁切图片
     */
    public static Bitmap ImageCrop(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

        int retX = w > h ? (w - h) / 2 : 0;//基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;

        //下面这句是关键
        return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    //截取数字
    public static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * @param context
     * @return 获取屏幕的宽高 单位px  返回整形数字
     */
    public int[]  getHandW(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm=new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        int width2=dm.widthPixels;
        int height2=dm.heightPixels;
        int[] HW=new int[2];
        HW[0]=width2;
        HW[1]=height2;
        Log.d("width", String.valueOf(width2));
        Log.d("height", String.valueOf(height2));
        return HW;
    }

    public boolean isNetworkAvailable(Context context) {
//        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());// 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 两张图片是否一样
     * @param b1
     * @param b2
     * @return
     */
    public static  boolean isEquals(Bitmap b1,Bitmap b2){
        if(b1.getWidth()==b2.getWidth()
                &&b1.getHeight()==b2.getHeight()){
            int xCount = b1.getWidth();
            int yCount = b1.getHeight();
            for(int x=0; x<xCount; x++){
                for(int y=0; y<yCount; y++){
                    if(b1.getPixel(x, y)!=b2.getPixel(x, y)){
                        return false;
                    }
                }
            }
            return true;
        }else{
            return false;
        }
    }

    /**
     * @param context
     * @return 如果手机使用的是中文 就返回true 否则返回false
     */
    public boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

    /**
     * @return 获取代码当前的行数
     */
    public static String getLineInfo() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getFileName() + ": Line " + ste.getLineNumber();
    }

    /**返回获取当前时间的字符串
     * @return
     */
    public static String GetCurrentData(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate=new Date(System.currentTimeMillis());//获取当前时间
        String  str =formatter.format(curDate);
        return str;
    }

    /**
     *  把bitmap 换成圆角bitmap
     * @param sourceBitmap
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap sourceBitmap) {
        try {
            Bitmap targetBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), Bitmap.Config.ARGB_8888);
// 得到画布
            Canvas canvas = new Canvas(targetBitmap);
// 创建画笔
            Paint paint = new Paint();
            paint.setAntiAlias(true);
// 值越大角度越明显
            float roundPx = 30;
            float roundPy = 30;
            Rect rect = new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
            RectF rectF = new RectF(rect);
            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawRoundRect(rectF, roundPx, roundPy, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(sourceBitmap, rect, rect, paint);
            return targetBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
        return null;
    }
}
