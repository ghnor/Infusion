package com.freer.infusion.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.PowerManager;
import android.support.v7.widget.SwitchCompat;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 2172980000773 on 2016/5/11.
 */
public class CommonUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将String转为HashMap<String, String>
     */
    public static HashMap<String, String> strToHash(String str) {

        HashMap<String, String> strMap = new HashMap<>();

        String[] strArray = str.split(",");

        if (strArray != null){
            for (int i = 0, y = strArray.length; i <y; i++) {
                strMap.put("" + strArray[i], "" + strArray[i]);
            }
        }


        return strMap;
    }

    /**
     * 将HashMap<String, String>转为String
     */
    public static String hashToString(HashMap<String, String> hashMap) {

        String str = "";

        if (hashMap == null){
            hashMap = new HashMap<>();
        }
        Iterator iter = hashMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();

            if ("".equals(str)){
                str += val.toString();
            } else {
                str += "," + val.toString();
            }
        }

        return str;
    }

    /**
     * 将选择的床位号保存为HashMap<String, String>形式
     */
    public static HashMap<String, String> saveAsHashMap(ArrayList<SwitchCompat> checkList) {

        HashMap<String, String> hashMap = new HashMap<>();

        if (checkList != null){
            for (SwitchCompat switchCompat: checkList) {
                if (switchCompat.isChecked()){
                    hashMap.put("" + switchCompat.getId(), "" + switchCompat.getId());
                }
            }
        }

        return hashMap;
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals("cn.igap.sale")
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    // 判断手机是否在锁屏状态
    public static boolean isAppLight(Context context) {
        PowerManager pm = (PowerManager) context.getApplicationContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了
        return isScreenOn;
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}