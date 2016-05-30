package com.freer.infusion.util;

/**
 * Created by 2172980000774 on 2016/4/27.
 */
import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 * @author WikerYong   Email:<a href="#">yw_312@foxmail.com</a>
 * @version 2012-5-21 下午9:21:01
 */
public class ToastUtils {

    private ToastUtils() {};
    private static class ToastUtilsHolder {
        private static final ToastUtils INSTANCE = new ToastUtils();
    }
    public static final ToastUtils getInstance() {
        return ToastUtilsHolder.INSTANCE;
    }

    private Toast mToast = null;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public void showShort(Context context, CharSequence message) {
        showToast(context, message.toString(), Toast.LENGTH_SHORT);
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public void showShort(Context context, int message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public void showLong(Context context, CharSequence message) {
        showToast(context, message.toString(), Toast.LENGTH_LONG);
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public void showLong(Context context, int message) {
        showToast(context, message, Toast.LENGTH_LONG);
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public void show(Context context, CharSequence message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public void show(Context context, int message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

    /**
     * Toast发送消息
     * @author WikerYong   Email:<a href="#">yw_312@foxmail.com</a>
     * @version 2012-5-22 上午11:14:09
     * @param context
     * @param message
     * @param duration
     */
    private void showToast(Context context, int message, int duration) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, message, duration);
        mToast.show();
    }

    /**
     * Toast发送消息
     * @author WikerYong   Email:<a href="#">yw_312@foxmail.com</a>
     * @version 2012-5-22 上午11:14:27
     * @param context
     * @param message
     * @param duration
     */
    private void showToast(Context context, String message, int duration) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, message, duration);
        mToast.show();
    }

    /**
     * 销毁单例保存的变量
     */
    public void destroy() {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = null;
    }
}
