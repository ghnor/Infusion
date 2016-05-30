package com.freer.infusion.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.Log;

import com.freer.infusion.module.main.MainActivity;
import com.freer.infusion.module.service.SocketService;

/**
 * Created by 2172980000773 on 2016/5/16.
 */
public class ScreenLockLocation {

    private AlarmManager mAlarmManger = null;
    private TimerReceiver mLister = null;
    private PendingIntent pi = null;
    private Context mContext = null;
    private final String actionStr = "com.freer.infusion.starting";
    private final String SERVICE_NAME = "com.freer.infusion.SocketService";
    private PowerManager.WakeLock wl = null;

    public ScreenLockLocation(Context context) {
        mContext = context.getApplicationContext();
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);

        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LocationWackLock"); // 设置CPU在锁屏状态下可以继续操作一些服务
        wl.setReferenceCounted(false);
    }

    // 开启锁屏锁
    public void start() {
        mAlarmManger = (AlarmManager) mContext
                .getSystemService(Context.ALARM_SERVICE);
        mLister = new TimerReceiver();
        mContext.registerReceiver(mLister, new IntentFilter(actionStr)); // 注册广播
        pi = PendingIntent.getBroadcast(mContext, 0, new Intent(actionStr),
                PendingIntent.FLAG_UPDATE_CURRENT); // 发送的广播类型
        mAlarmManger.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,5000, pi); // 系统的全局闹钟，每过5秒，发送一个广播
    }

    // 暂停锁屏锁
    public void stop(){

        try {
            mContext.unregisterReceiver(mLister);
            mAlarmManger.cancel(pi);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Receiver not registered")) {
                // Ignore this exception. This is exactly what is desired
            } else {
                // unexpected, re-throw
                throw e;
            }
        }

    }

    // 注销锁屏锁
    public void releaseWackLock(){
        if(wl != null && wl.isHeld()) wl.release();
    }

    public class TimerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(actionStr)) {

                // 如果App在后台，那么就自动将App启动到前台
                if (!CommonUtil.isAppOnForeground(mContext) && !CommonUtil.isAppLight(mContext)){
                    Intent LOCIntent = new Intent(mContext, MainActivity.class);
                    LOCIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(LOCIntent);
                }

                // 如果SocketService没有在运行，那么就启动他
                if (CommonUtil.isServiceWork(mContext, SERVICE_NAME)){
                    Intent serviceIntent = new Intent(mContext, SocketService.class);
                    mContext.startService(serviceIntent);
                }

            }
        }

    }

}
