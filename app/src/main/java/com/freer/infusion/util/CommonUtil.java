package com.freer.infusion.util;

import android.content.Context;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    public static HashMap<String, String> saveAsHashMap(ArrayList<CheckBox> checkList) {

        HashMap<String, String> hashMap = new HashMap<>();

        if (checkList != null){
            for (CheckBox checkBox: checkList) {
                if (checkBox.isChecked()){
                    hashMap.put("" + checkBox.getId(), "" + checkBox.getId());
                }
            }
        }

        return hashMap;
    }

}
