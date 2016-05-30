package com.freer.infusion.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import com.freer.infusion.App;

import java.io.IOException;

/**
 * Created by 2172980000774 on 2016/5/16.
 */
public class MediaHelper {

    public static void startRingtone() {
        try {
            // 使用来电铃声的铃声路径
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            // 如果为空，才构造，不为空，说明之前有构造过
            MediaPlayer mMediaPlayer = null;
            if(mMediaPlayer == null)
                mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(App.getAppContext(), uri);
            mMediaPlayer.setLooping(false);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void startVibrate() {
        Vibrator vibrator = (Vibrator) App.getAppContext()
                .getSystemService(App.getAppContext().VIBRATOR_SERVICE);
        // 等待0秒，震动1秒，
        vibrator.vibrate(new long[]{0, 1000}, -1);
    }

}
