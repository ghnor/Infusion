package com.freer.infusion.config;

import android.hardware.camera2.params.ColorSpaceTransform;

import com.freer.infusion.util.SPUtils;

/**
 * Created by 2172980000774 on 2016/5/11.
 */
public class AppConfig {

    // 线程安全，延迟加载
    private AppConfig() {}
    private static class AppConfigHolder {
        private static final AppConfig INSTANCE = new AppConfig();
    }
    public static final AppConfig getInstance() {
        return AppConfigHolder.INSTANCE;
    }

    /* sp存储key */
    private static final String SP_SOUND = "SP_SOUND"; //声音
    private static final String SP_SHARK = "SP_SHARK"; //震动
    private static final String SP_SPEED = "SP_SPEED"; //滴速提示
    private static final String SP_SET_ALERT = "SP_SET_ALERT"; //自动弹出设置框
    private static final String SP_SET_SYSTEM = "SP_SET_SYSTEM"; //启动时打开系统设置页面
    private static final String SP_VERSION = "SP_VERSION"; //版本设置
    /* 临时变量 */
    private boolean sound; //声音
    private boolean shark; //震动
    private boolean speed; //滴速提示
    private boolean setAlert; //自动弹出设置框
    private boolean setSystem; //启动时打开系统设置页面
    private boolean version; //版本设置

    /**
     * 清空单例中的全部变量
     */
    public void destory() {

    }

    /**
     * 设置声音提示
     * @param b
     */
    public void setSoundHint(boolean b) {
        sound = b;
        SPUtils.put(SP_SOUND, b);
    }

    /**
     * 获取声音提示是否打开
     * @return
     */
    public boolean isSoundHint() {
        if (SPUtils.contains(SP_SOUND)) {
            return (boolean) SPUtils.get(SP_SOUND, sound);
        }
        return sound;
    }

    /**
     * 设置震动提示
     * @param b
     */
    public void setSharkHint(boolean b) {
        shark = b;
        SPUtils.put(SP_SHARK, b);
    }

    /**
     * 获取震动提示是否打开
     * @return
     */
    public boolean isSharkHint() {
        if (SPUtils.contains(SP_SHARK)) {
            return (boolean) SPUtils.get(SP_SHARK, shark);
        }
        return shark;
    }

    /**
     * 设置滴速上下限警示
     * @param b
     */
    public void setSpeedWarn(boolean b) {
        speed = b;
        SPUtils.put(SP_SPEED, b);
    }

    /**
     * 获取上下限警示是否开启
     * @return
     */
    public boolean isSpeedWarn() {
        if (SPUtils.contains(SP_SPEED)) {
            return (boolean) SPUtils.get(SP_SPEED, speed);
        }
        return speed;
    }

    /**
     * 设置是否自动弹出设置框
     * @param b
     */
    public void setAlertOpen(boolean b) {
        setAlert = b;
        SPUtils.put(SP_SET_ALERT, b);
    }

    /**
     * 获取是否自动弹出设置框
     * @return
     */
    public boolean isAlertOpen() {
        if (SPUtils.contains(SP_SET_ALERT)) {
            return (boolean) SPUtils.get(SP_SET_ALERT, setAlert);
        }
        return setAlert;
    }

    /**
     * 设置启动时是否打开系统设置页
     * @param b
     */
    public void setSystemOpen(boolean b) {
        setSystem = b;
        SPUtils.put(SP_SET_SYSTEM, b);
    }

    /**
     * 获取启动时是否打开系统设置页
     * @return
     */
    public boolean isSystemOpen() {
        if (SPUtils.contains(SP_SET_SYSTEM)) {
            return (boolean) SPUtils.get(SP_SET_SYSTEM, setSystem);
        }
        return setSystem;
    }

    /**
     * 设置版本
     * @param b
     */
    public void setVersion(boolean b) {
        version = b;
        SPUtils.put(SP_VERSION, b);
    }

    /**
     * 获取版本
     * @return
     */
    public boolean isVersion() {
        if (SPUtils.contains(SP_VERSION)) {
            return (boolean) SPUtils.get(SP_VERSION, version);
        }
        return version;
    }

}
