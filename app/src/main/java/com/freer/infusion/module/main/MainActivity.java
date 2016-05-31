package com.freer.infusion.module.main;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.freer.infusion.R;
import com.freer.infusion.base.BaseActivity;
import com.freer.infusion.entity.SocketEntity;
import com.freer.infusion.model.SocketDataProcess;
import com.freer.infusion.module.Set.BedSetActivity;
import com.freer.infusion.module.Set.SetActivity;
import com.freer.infusion.module.service.SocketService;
import com.freer.infusion.module.us.AboutUsActivity;
import com.freer.infusion.util.DialogManger;
import com.freer.infusion.util.ToastUtils;
import com.igexin.sdk.PushManager;
import com.qihoo.updatesdk.lib.UpdataHelper;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements SocketService.IReceiveMessage, IFrag2Act {

    private MainFragment mFllowFragment; //关注床位界面
    private MainFragment mAllFragment; //所有床位界面

    private Toolbar mToolbar; //标题栏
    private TabLayout mTabLayout; //标签页tip
    private ViewPager mViewPager; //标签页
    private TabFragmentAdapter mTabFragmentAdapter; //标签页适配器

    private Dialog mProgressDialog = null; //转圈对话框
    private Dialog mRetryDialog = null; //连接重试对话框

    private MessageBackReciver mReciver;

    private IntentFilter mIntentFilter;

    private LocalBroadcastManager mLocalBroadcastManager;

    private Intent mServiceIntent;

    private SocketService.SocketBinder mSocketBinder;
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mSocketBinder = (SocketService.SocketBinder) service;
            mSocketBinder.setOnReveiveMessage(MainActivity.this);
            mFllowFragment.setBinder(mSocketBinder);
            mAllFragment.setBinder(mSocketBinder);
            mFllowFragment.setIFrag2Act(MainActivity.this);
            mAllFragment.setIFrag2Act(MainActivity.this);
        }
    };

    @Override
    public void receiveMessage(List<SocketEntity> followBedList, List<SocketEntity> allBedList) {
        mFllowFragment.setData(followBedList);
        mAllFragment.setData(allBedList);
    }

    @Override
    public void sendFailed() {
        if (mRetryDialog != null && !mRetryDialog.isShowing()) {
            mRetryDialog.show();
        }
    }

    class MessageBackReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(SocketService.HEART_BEAT_ACTION)) {

            } else if (action.equals(SocketService.CONN_CHECK_ACTION)) {
                mProgressDialog.dismiss();
            } else if (action.equals(SocketService.MESSAGE_ACTION)){

            } else if (action.equals(SocketService.CONN_ERROR_ACTION)) {
                mProgressDialog.dismiss();
                mRetryDialog.show();
            } else if (action.equals(SocketService.NO_CONN_ACTION)) {
                mProgressDialog.dismiss();
                mRetryDialog.show();
            }
//            else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
//                Parcelable parcelableExtra = intent
//                        .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
//                if (null != parcelableExtra) {
//                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
//                    NetworkInfo.State state = networkInfo.getState();
//                    boolean isConnected = state == NetworkInfo.State.CONNECTED;
//                    // 连入一个有效的wifi
//                    if (isConnected) {
//                        mSocketBinder.reStart();
//                    }
//                }
//            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 标题栏
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.string_title_main);

        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mFllowFragment = new MainFragment();
        mAllFragment = new MainFragment();

        mTabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager());
        mTabFragmentAdapter.addFragment(mFllowFragment, getString(R.string.main_att_bed));
        mTabFragmentAdapter.addFragment(mAllFragment, getString(R.string.main_all_bed));

        mViewPager.setAdapter(mTabFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        // 注册广播
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mReciver = new MessageBackReciver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(SocketService.HEART_BEAT_ACTION);
        mIntentFilter.addAction(SocketService.MESSAGE_ACTION);
        mIntentFilter.addAction(SocketService.CONN_CHECK_ACTION);
        mIntentFilter.addAction(SocketService.CONN_ERROR_ACTION);
        mIntentFilter.addAction(SocketService.NO_CONN_ACTION);
//        mIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);

        // 启动service
        mServiceIntent = new Intent(this, SocketService.class);
        startService(mServiceIntent);
        bindService(mServiceIntent, conn, BIND_AUTO_CREATE);

        mRetryDialog = DialogManger.getRetryDialog(this,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSocketBinder.reStart();
                        mProgressDialog.show();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopWork();
                    }
                });

        // 初始化个推
        PushManager.getInstance().initialize(this.getApplicationContext());
        // 初始化360更新
        UpdataHelper.getInstance().init(getApplicationContext(), Color.parseColor("#43A047"));
        // 检查更新
//        UpdataHelper.getInstance().manualUpdate("com.freer.infusion");

        mProgressDialog = DialogManger.getProgressDialog(this);
        mProgressDialog.show();
    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        mLocalBroadcastManager.unregisterReceiver(mReciver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = new Intent();
        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_setting) {
            intent.setClass(MainActivity.this, SetActivity.class);
            this.startActivity(intent);
            return true;
        } else if (id == R.id.menu_aboutus) {
            intent.setClass(MainActivity.this, AboutUsActivity.class);
            this.startActivity(intent);
            return true;
        } else if (id == R.id.menu_quit) {
            DialogManger.getQuitDialog(MainActivity.this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    stopWork();
                }
            }).show();
            return true;
        } else if (id == R.id.menu_bed) {
            intent.setClass(MainActivity.this, BedSetActivity.class);
            this.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 手动关闭APP
     */
    public void stopWork() {
        MainActivity.this.stopService(mServiceIntent);
        MainActivity.this.finish();
        ActivityManager activityMgr= (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        activityMgr.killBackgroundProcesses(getPackageName());
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
