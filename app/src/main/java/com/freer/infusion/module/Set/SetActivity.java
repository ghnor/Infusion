package com.freer.infusion.module.Set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.freer.infusion.R;
import com.freer.infusion.base.BaseActivity;
import com.freer.infusion.config.AppConfig;
import com.freer.infusion.util.ToastUtils;

public class SetActivity extends BaseActivity implements OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Context mContext;

    /* activity布局控件 */
    private Toolbar mToolbar; // 标题栏
    private SwitchCompat mToggleSound; // 提示音
    private SwitchCompat mToggleShark; // 震动
    private SwitchCompat mToggleWarning; // 滴速上下限警示
    private SwitchCompat mToggleFrame; // 自动弹出设置框
    private SwitchCompat mToggleOpen; // 启动时打开系统设置页
    private LinearLayout mLinearEdition; // 版本设置按钮
    private LinearLayout mLinearBed; // 床位设置按钮
    private LinearLayout mLinearServer; // 服务器设置按钮
    private TextView mTxtvEdition; // 版本的内容信息
    private TextView mTxtvBed; // 床位选择的信息
    private TextView mTxtvServer; // 连接的服务器信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        mContext = this;

        initView();
        initEvent();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mToggleSound = (SwitchCompat) findViewById(R.id.toggle_set_sound);
        mToggleShark = (SwitchCompat) findViewById(R.id.toggle_set_shark);
        mToggleWarning = (SwitchCompat) findViewById(R.id.toggle_set_warning);
        mToggleFrame = (SwitchCompat) findViewById(R.id.toggle_set_frame);
        mToggleOpen = (SwitchCompat) findViewById(R.id.toggle_set_open);
        mLinearEdition = (LinearLayout) findViewById(R.id.linear_set_edition);
        mLinearBed = (LinearLayout) findViewById(R.id.linear_set_bed);
        mLinearServer = (LinearLayout) findViewById(R.id.linear_set_server);
        mTxtvEdition = (TextView) findViewById(R.id.txtv_set_edition);
        mTxtvBed = (TextView) findViewById(R.id.txtv_set_bed);
        mTxtvServer = (TextView) findViewById(R.id.txtv_set_server);

        AppConfig appConfig = AppConfig.getInstance();
        mToggleSound.setChecked(appConfig.isSoundHint());
        mToggleShark.setChecked(appConfig.isSharkHint());
        mToggleWarning.setChecked(appConfig.isSpeedWarn());
        mToggleFrame.setChecked(appConfig.isAlertOpen());
        mToggleOpen.setChecked(appConfig.isSystemOpen());
    }

    private void initEvent() {
        mToggleSound.setOnCheckedChangeListener(this);
        mToggleShark.setOnCheckedChangeListener(this);
        mToggleWarning.setOnCheckedChangeListener(this);
        mToggleFrame.setOnCheckedChangeListener(this);
        mToggleOpen.setOnCheckedChangeListener(this);
        mLinearEdition.setOnClickListener(this);
        mLinearBed.setOnClickListener(this);
        mLinearServer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = null;

        switch (v.getId()){
            case R.id.linear_set_edition:
                // 点击版本设置按钮
                break;
            case R.id.linear_set_bed:
                // 点击床位设置按钮
                intent = new Intent(mContext, BedSetActivity.class);
                startActivity(intent);
                break;
            case R.id.linear_set_server:
                // 点击服务器设置按钮

                //测试
                ToastUtils.getInstance().showShort(SetActivity.this,
                        ""+AppConfig.getInstance().isSoundHint()+AppConfig.getInstance().isSharkHint());

                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()){
            case R.id.toggle_set_sound:
                AppConfig.getInstance().setSoundHint(isChecked);
                if (isChecked){
                    // 打开声音
                } else {
                    // 关闭声音
                }
                break;
            case R.id.toggle_set_shark:
                AppConfig.getInstance().setSharkHint(isChecked);
                if (isChecked){
                    // 打开震动
                } else {
                    // 关闭震动
                }
                break;
            case R.id.toggle_set_warning:
                AppConfig.getInstance().setSpeedWarn(isChecked);
                if (isChecked){
                    // 打开警示
                } else {
                    // 关闭警示
                }
                break;
            case R.id.toggle_set_frame:
                AppConfig.getInstance().setAlertOpen(isChecked);
                if (isChecked){
                    // 打开弹窗
                } else {
                    // 关闭弹窗
                }
                break;
            case R.id.toggle_set_open:
                AppConfig.getInstance().setSystemOpen(isChecked);
                if (isChecked){
                    // 打开自打开
                } else {
                    // 关闭自打开
                }
                break;
            default:
                break;
        }
    }
}
