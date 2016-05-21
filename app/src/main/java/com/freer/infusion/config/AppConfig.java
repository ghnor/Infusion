package com.freer.infusion.config;

import android.hardware.camera2.params.ColorSpaceTransform;

import com.freer.infusion.util.CommonUtil;
import com.freer.infusion.util.SPUtils;

import java.util.HashMap;

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
    private static final String SP_SOUND_FAST = "SP_SOUND_FAST"; // 声音(滴速过快)
    private static final String SP_SOUND_SLOW = "SP_SOUND_SLOW"; // 声音(滴速过慢)
    private static final String SP_SOUND_STOP = "SP_SOUND_STOP"; // 声音(滴液停止)
    private static final String SP_SOUND_LOW_POWER = "SP_SOUND_LOW_POWER"; // 声音(低电)
    private static final String SP_SHARK_FAST = "SP_SHARK_FAST"; // 震动(滴速过快)
    private static final String SP_SHARK_SLOW = "SP_SHARK_SLOW"; // 震动(滴速过慢)
    private static final String SP_SHARK_STOP = "SP_SHARK_STOP"; // 震动(滴液停止)
    private static final String SP_SHARK_LOW_POWER = "SP_SHARK_LOW_POWER"; // 震动(低电)
    private static final String SP_MODE = "SP_MODE"; // 模式设置
    private static final String SP_SERVER_IP = "SP_SERVER_IP"; // 服务器设置(IP)
    private static final String SP_SERVER_PORT = "SP_SERVER_PORT"; // 服务器设置(端口)
    private static final String SP_MY_BED = "SP_MY_BED"; // 我的床位
    private static final String SP_FOLLOW_BED = "SP_FOLLOW_BED"; // 我的关注
    /* 临时变量 */
    private boolean soundFast; // 声音(滴速过快)
    private boolean soundSlow; // 声音(滴速过慢)
    private boolean soundStop; // 声音(滴液停止)
    private boolean soundLowPower; // 声音(低电)
    private boolean sharkFast; // 震动(滴速过快)
    private boolean sharkSlow; // 震动(滴速过慢)
    private boolean sharkStop; // 震动(滴液停止)
    private boolean sharkLowPower; // 震动(低电)
    private int mode; // 版本设置(0:标准版  1：自选版)
    private String serverIp = ""; // 服务器设置的信息(IP)
    private String serverPort = ""; // 服务器设置的信息(端口)
    private HashMap<String, String> myBedMap; // 我的床位
    private HashMap<String, String> followBedMap; // 我关注的床位

    /**
     * 清空单例中的全部变量
     */
    public void destory() {

    }

    /**
     * 设置声音(过快)提示
     * @param b
     */
    public void setSoundFast(boolean b) {
        soundFast = b;
        SPUtils.put(SP_SOUND_FAST, b);
    }

    /**
     * 获取声音（过快）提示是否打开
     * @return
     */
    public boolean isSoundFast() {
        if (SPUtils.contains(SP_SOUND_FAST)) {
            soundFast = (boolean) SPUtils.get(SP_SOUND_FAST, soundFast);
            return soundFast;
        }
        return soundFast;
    }

    /**
     * 设置声音（过慢）提示
     * @param b
     */
    public void setSoundSlow(boolean b) {
        soundSlow = b;
        SPUtils.put(SP_SOUND_SLOW, b);
    }

    /**
     * 获取声音（过慢）提示是否打开
     * @return
     */
    public boolean isSoundSlow() {
        if (SPUtils.contains(SP_SOUND_SLOW)) {
            soundSlow = (boolean) SPUtils.get(SP_SOUND_SLOW, soundSlow);
            return soundSlow;
        }
        return soundSlow;
    }

    /**
     * 设置声音（停止）提示
     * @param b
     */
    public void setSoundStop(boolean b) {
        soundStop = b;
        SPUtils.put(SP_SOUND_STOP, b);
    }

    /**
     * 获取声音（停止）提示是否打开
     * @return
     */
    public boolean isSoundStop() {
        if (SPUtils.contains(SP_SOUND_STOP)) {
            soundStop = (boolean) SPUtils.get(SP_SOUND_STOP, soundStop);
            return soundStop;
        }
        return soundStop;
    }

    /**
     * 设置声音（低电）提示
     * @param b
     */
    public void setSoundLowPower(boolean b) {
        soundLowPower = b;
        SPUtils.put(SP_SOUND_LOW_POWER, b);
    }

    /**
     * 获取声音（低电）提示是否打开
     * @return
     */
    public boolean isSoundLowPower() {
        if (SPUtils.contains(SP_SOUND_LOW_POWER)) {
            soundLowPower = (boolean) SPUtils.get(SP_SOUND_LOW_POWER, soundLowPower);
            return soundLowPower;
        }
        return soundLowPower;
    }

    /**
     * 设置震动提示
     * @param b
     */
    public void setSharkFast(boolean b) {
        sharkFast = b;
        SPUtils.put(SP_SHARK_FAST, b);
    }

    /**
     * 获取震动提示是否打开
     * @return
     */
    public boolean isSharkFast() {
        if (SPUtils.contains(SP_SHARK_FAST)) {
            sharkFast = (boolean) SPUtils.get(SP_SHARK_FAST, sharkFast);
            return sharkFast;
        }
        return sharkFast;
    }

    /**
     * 设置震动提示
     * @param b
     */
    public void setSharkSlow(boolean b) {
        sharkSlow = b;
        SPUtils.put(SP_SHARK_SLOW, b);
    }

    /**
     * 获取震动提示是否打开
     * @return
     */
    public boolean isSharkSlow() {
        if (SPUtils.contains(SP_SHARK_SLOW)) {
            sharkSlow = (boolean) SPUtils.get(SP_SHARK_SLOW, sharkSlow);
            return sharkSlow;
        }
        return sharkSlow;
    }

    /**
     * 设置震动提示
     * @param b
     */
    public void setSharkStop(boolean b) {
        sharkStop = b;
        SPUtils.put(SP_SHARK_STOP, b);
    }

    /**
     * 获取震动提示是否打开
     * @return
     */
    public boolean isSharkStop() {
        if (SPUtils.contains(SP_SHARK_STOP)) {
            sharkStop = (boolean) SPUtils.get(SP_SHARK_STOP, sharkStop);
            return sharkStop;
        }
        return sharkStop;
    }

    /**
     * 设置震动提示
     * @param b
     */
    public void setSharkLowPower(boolean b) {
        sharkLowPower = b;
        SPUtils.put(SP_SHARK_LOW_POWER, b);
    }

    /**
     * 获取震动提示是否打开
     * @return
     */
    public boolean isSharkLowPower() {
        if (SPUtils.contains(SP_SHARK_LOW_POWER)) {
            sharkLowPower = (boolean) SPUtils.get(SP_SHARK_LOW_POWER, sharkLowPower);
            return sharkLowPower;
        }
        return sharkLowPower;
    }

    /**
     * 设置版本
     * @param i
     */
    public void setMode(int i) {
        mode = i;
        SPUtils.put(SP_MODE, i);
    }

    /**
     * 获取版本
     * @return
     */
    public int getMode() {
        if (SPUtils.contains(SP_MODE)) {
            mode = (int) SPUtils.get(SP_MODE, mode);
            return mode;
        }
        return mode;
    }

    /**
     * 设置服务器IP
     * @param str
     */
    public void setServerIp(String str) {
        serverIp = str;
        SPUtils.put(SP_SERVER_IP, str);
    }

    /**
     * 获取服务器IP
     * @return
     */
    public String getServerIp() {
        if (SPUtils.contains(SP_SERVER_IP)) {
            serverIp = (String) SPUtils.get(SP_SERVER_IP, serverIp);
            if (serverIp == null){
                serverIp = "";
            }
            return serverIp;
        }

        if (serverIp == null){
            serverIp = "";
        }
        return serverIp;
    }

    /**
     * 设置服务器端口
     * @param str
     */
    public void setServerPort(String str) {
        serverPort = str;
        SPUtils.put(SP_SERVER_PORT, str);
    }

    /**
     * 获取服务器端口
     * @return
     */
    public String getServerPort() {
        if (SPUtils.contains(SP_SERVER_PORT)) {
            serverPort = (String) SPUtils.get(SP_SERVER_PORT, serverPort);
            if (serverPort == null){
                serverPort = "";
            }
            return serverPort;
        }

        if (serverPort == null){
            serverPort = "";
        }
        return serverPort;
    }

    /**
     * 设置我的床位
     * @param hashMap
     */
    public void setMyBed(HashMap<String, String> hashMap) {
        myBedMap = hashMap;
        SPUtils.put(SP_MY_BED, CommonUtil.hashToString(myBedMap));
    }

    /**
     * 得到我的床位
     * @return
     */
    public HashMap<String, String> getMyBed() {
        if (SPUtils.contains(SP_MY_BED)) {
            myBedMap = CommonUtil.strToHash((String) SPUtils.get(SP_MY_BED, CommonUtil.hashToString(myBedMap)));
            return myBedMap;
        }
        return myBedMap;
    }

    /**
     * 设置关注床位
     * @param hashMap
     */
    public void setFollowBed(HashMap<String, String> hashMap) {
        followBedMap = hashMap;
        SPUtils.put(SP_FOLLOW_BED, CommonUtil.hashToString(followBedMap));
    }

    /**
     * 得到关注床位
     * @return
     */
    public HashMap<String, String> getFollowBed() {
        if (SPUtils.contains(SP_FOLLOW_BED)) {
            followBedMap = CommonUtil.strToHash((String) SPUtils.get(SP_FOLLOW_BED, CommonUtil.hashToString(followBedMap)));
            return followBedMap;
        }
        return followBedMap;
    }

    /**
     * 是否是调试版本
     */
    public boolean isDebug() {
        return true;
    }
}
