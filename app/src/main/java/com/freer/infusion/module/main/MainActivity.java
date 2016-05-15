package com.freer.infusion.module.main;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.freer.infusion.R;
import com.freer.infusion.base.BaseActivity;
import com.freer.infusion.model.SocketDataProcess;
import com.freer.infusion.module.Set.SetActivity;
import com.freer.infusion.module.SocketTestActivity;
import com.freer.infusion.module.service.SocketService;
import com.freer.infusion.util.DialogManger;

public class MainActivity extends BaseActivity implements SocketService.IReceiveMessage {

    private MainFragment mFllowFragment; //关注床位界面
    private MainFragment mAllFragment; //所有床位界面

    private Toolbar mToolbar; //标题栏
    private TabLayout mTabLayout; //标签页tip
    private ViewPager mViewPager; //标签页
    private TabFragmentAdapter mTabFragmentAdapter; //标签页适配器

    private SocketDataProcess mProcess = new SocketDataProcess();

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
            mSocketBinder.setIpPort("192.168.1.5", 2020);
        }
    };

    class MessageBackReciver extends BroadcastReceiver {

        public MessageBackReciver() {}

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(SocketService.HEART_BEAT_ACTION)) {
            } else {
                String message = intent.getStringExtra("message");
                mProcess.processData(message);
                mFllowFragment.setData(mProcess.getFollowBed());
                mAllFragment.setData(mProcess.getAllBed());
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("onCreate");
        // 标题栏
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mFllowFragment = new MainFragment();
        mAllFragment = new MainFragment();

        mTabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager());
        mTabFragmentAdapter.addFragment(mFllowFragment, "关注床位");
        mTabFragmentAdapter.addFragment(mAllFragment, "全部床位");

        mViewPager.setAdapter(mTabFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

        mReciver = new MessageBackReciver();

        mServiceIntent = new Intent(this, SocketService.class);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(SocketService.HEART_BEAT_ACTION);
        mIntentFilter.addAction(SocketService.MESSAGE_ACTION);

        mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
        bindService(mServiceIntent, conn, BIND_AUTO_CREATE);
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
            intent.setClass(MainActivity.this, SocketTestActivity.class);
            this.startActivity(intent);
            return true;
        } else if (id == R.id.menu_quit) {
            DialogManger.getQuitDialog(MainActivity.this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.finish();
                }
            }).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void receiveMessage(String message) {

    }
}
