package com.freer.infusion.module.Set;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.freer.infusion.R;
import com.freer.infusion.base.BaseActivity;
import com.freer.infusion.config.AppConfig;
import com.freer.infusion.util.SPUtils;
import com.freer.infusion.util.ToastUtils;

import org.w3c.dom.Text;

import java.util.zip.Inflater;

public class SetActivity extends BaseActivity implements OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Context mContext;
    private AppConfig mAppConfig; // app设置的方法

    /* activity布局控件 */
    private Toolbar mToolbar; // 标题栏
    private LinearLayout mLinearSound; // 设置是否开启声音
    private LinearLayout mLinearShark; // 设置是否开启震动
    private LinearLayout mLinearEdition; // 版本设置按钮
    private LinearLayout mLinearBed; // 床位设置按钮
    private LinearLayout mLinearServer; // 服务器设置按钮
    private TextView mTxtvEdition; // 版本的内容信息
    private TextView mTxtvBed; // 床位选择的信息
    private TextView mTxtvServer; // 连接的服务器信息
    private Button mBtnBack; // 返回首页按钮

    /* 声音和震动设置Dialog */
    private Dialog mDialog; // 弹出框
    private TextView mTxtvDialogTitle; // 设置dialog的标题
    private SwitchCompat mSwitchDialogFast; // 滴速过快设置按钮
    private SwitchCompat mSwitchDialogSLow; // 滴速过慢设置按钮
    private SwitchCompat mSwitchDialogStop; // 滴液停止设置按钮
    private SwitchCompat mSwitchDialogPower; // 低电量设置按钮
    private Button mBtnDialogCancel; // dialog取消按钮

    /* 模式选择Dialog */
    private Dialog mModeDialog; // 模式弹出框
    private Button mBtnstandard; // 标准模式按钮
    private Button mBtnOptional; // 自选模式按钮
    private Button mBtnModeCancel; // 模式选择取消按钮

    /* 服务器设置Dialog */
    private Dialog mServerDialog; // 服务器弹出框
    private EditText mEditAddress; // IP地址输入框
    private EditText mEditPort; // 端口输入框
    private Button mBtnServerOk; // 服务器设置确定按钮
    private Button mBtnServerCancel; // 服务器设置取消按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        mContext = this;
        mAppConfig = AppConfig.getInstance();

        initView();
        initEvent();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mLinearSound = (LinearLayout) findViewById(R.id.linear_set_sound);
        mLinearShark = (LinearLayout) findViewById(R.id.linear_set_shark);
        mLinearEdition = (LinearLayout) findViewById(R.id.linear_set_edition);
        mLinearBed = (LinearLayout) findViewById(R.id.linear_set_bed);
        mLinearServer = (LinearLayout) findViewById(R.id.linear_set_server);
        mTxtvEdition = (TextView) findViewById(R.id.txtv_set_edition);
        mTxtvBed = (TextView) findViewById(R.id.txtv_set_bed);
        mTxtvServer = (TextView) findViewById(R.id.txtv_set_server);
        mBtnBack = (Button) findViewById(R.id.btn_set_back);
    }

    private void initEvent() {

        mTxtvEdition.setText(mAppConfig.getMode() == 0 ? "标准版": "自选版");
        mTxtvServer.setText(mAppConfig.getServerIp() + " " + mAppConfig.getServerPort());

        mLinearSound.setOnClickListener(this);
        mLinearShark.setOnClickListener(this);
        mLinearEdition.setOnClickListener(this);
        mLinearBed.setOnClickListener(this);
        mLinearServer.setOnClickListener(this);
        mBtnBack.setOnClickListener(this);
    }

    /**
     * 初始化设置界面的弹窗（参数是标题的资源文件）
     * @param strTitleResource
     */
    private void initDialog(int strTitleResource) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View contentView = layoutInflater.inflate(R.layout.dialog_sound_set, null);
        mTxtvDialogTitle = (TextView) contentView.findViewById(R.id.txtv_set_title);
        mSwitchDialogFast = (SwitchCompat) contentView.findViewById(R.id.switch_fast_set);
        mSwitchDialogSLow = (SwitchCompat) contentView.findViewById(R.id.switch_slow_set);
        mSwitchDialogStop = (SwitchCompat) contentView.findViewById(R.id.switch_stop_set);
        mSwitchDialogPower = (SwitchCompat) contentView.findViewById(R.id.switch_low_power_set);
        mBtnDialogCancel = (Button) contentView.findViewById(R.id.btn_set_cancel);

        mTxtvDialogTitle.setText(strTitleResource);
        if (strTitleResource == R.string.string_sound_set){
            mSwitchDialogFast.setChecked(mAppConfig.isSoundFast());
            mSwitchDialogSLow.setChecked(mAppConfig.isSoundFastSlow());
            mSwitchDialogStop.setChecked(mAppConfig.isSoundStopt());
            mSwitchDialogPower.setChecked(mAppConfig.isSoundLowPowert());
        } else if(strTitleResource == R.string.string_shark_set) {
            mSwitchDialogFast.setChecked(mAppConfig.isSharkFast());
            mSwitchDialogSLow.setChecked(mAppConfig.isSharkSlow());
            mSwitchDialogStop.setChecked(mAppConfig.isSharkStop());
            mSwitchDialogPower.setChecked(mAppConfig.isSharkLowPower());
        }

        mSwitchDialogFast.setOnCheckedChangeListener(this);
        mSwitchDialogSLow.setOnCheckedChangeListener(this);
        mSwitchDialogStop.setOnCheckedChangeListener(this);
        mSwitchDialogPower.setOnCheckedChangeListener(this);
        mBtnDialogCancel.setOnClickListener(this);

        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(contentView);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth() - 80); //设置宽度
        mDialog.getWindow().setAttributes(lp);
        mDialog.show();

    }

    /**
     * 初始化模式选择界面的弹窗
     */
    private void initModeSelect() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View contentView = layoutInflater.inflate(R.layout.dialog_mode_select, null);
        mBtnstandard = (Button) contentView.findViewById(R.id.btn_mode_standard);
        mBtnOptional = (Button) contentView.findViewById(R.id.btn_mode_optional);
        mBtnModeCancel = (Button) contentView.findViewById(R.id.btn_mode_cancel);

        mBtnstandard.setOnClickListener(this);
        mBtnOptional.setOnClickListener(this);
        mBtnModeCancel.setOnClickListener(this);

        mModeDialog = new Dialog(mContext);
        mModeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mModeDialog.setContentView(contentView);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mModeDialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth() - 80); //设置宽度
        mModeDialog.getWindow().setAttributes(lp);
        mModeDialog.show();
    }

    /**
     * 初始化模式选择界面的弹窗
     */
    private void initServerSet() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View contentView = layoutInflater.inflate(R.layout.dialog_server_set, null);
        mEditAddress = (EditText) contentView.findViewById(R.id.edit_server_address);
        mEditPort = (EditText) contentView.findViewById(R.id.edit_server_port);
        mBtnServerOk = (Button) contentView.findViewById(R.id.btn_server_ok);
        mBtnServerCancel = (Button) contentView.findViewById(R.id.btn_server_cancel);

        mBtnServerOk.setOnClickListener(this);
        mBtnServerCancel.setOnClickListener(this);

        mServerDialog = new Dialog(mContext);
        mServerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mServerDialog.setContentView(contentView);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mServerDialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth() - 80); //设置宽度
        mServerDialog.getWindow().setAttributes(lp);
        mServerDialog.show();
    }

    @Override
    public void onClick(View v) {

        Intent intent = null;

        switch (v.getId()){
            case R.id.linear_set_sound:
                // 点击声音设置按钮
                initDialog(R.string.string_sound_set);
                break;
            case R.id.linear_set_shark:
                // 点击震动设置按钮
                initDialog(R.string.string_shark_set);
                break;
            case R.id.linear_set_edition:
                // 点击版本设置按钮
                initModeSelect();
                break;
            case R.id.linear_set_bed:
                // 点击床位设置按钮
                intent = new Intent(mContext, BedSetActivity.class);
                startActivity(intent);
                break;
            case R.id.linear_set_server:
                // 点击服务器设置按钮
                initServerSet();
                break;
            case R.id.btn_set_back:
                // 点击返回主界面按钮
                finish();
                break;
            case R.id.btn_set_cancel:
                // 点击Dialog上的取消键(关闭弹出框)
                mDialog.dismiss();
                break;
            case R.id.btn_mode_cancel:
                // 点击模式选择Dialog上的取消键(关闭弹出框)
                mModeDialog.dismiss();
                break;
            case R.id.btn_server_cancel:
                // 点击服务器设置Dialog上的取消键(关闭弹出框)
                mServerDialog.dismiss();
                break;
            case R.id.btn_mode_standard:
                // 选择标准版
                mAppConfig.setMode(0);
                mTxtvEdition.setText(R.string.string_mode_standard);
                mModeDialog.dismiss();
                break;
            case R.id.btn_mode_optional:
                // 选择自选版
                mAppConfig.setMode(1);
                mTxtvEdition.setText(R.string.string_mode_optional);
                mModeDialog.dismiss();
                break;
            case R.id.btn_server_ok:
                // 服务器设置确定按钮
                String strIP = mEditAddress.getText().toString();
                String strPort = mEditPort.getText().toString();
                if (strIP != null && !"".equals(strIP) && strPort != null && !"".equals(strPort)){
                    mAppConfig.setServerIp(strIP);
                    mAppConfig.setServerPort(strPort);
                    mTxtvServer.setText(strIP + " " + strPort);
                } else {
                    ToastUtils.getInstance().showShort(mContext, "请输入ＩＰ地址和端口号！");
                }
                mServerDialog.dismiss();
                break;

            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()){
            case R.id.switch_fast_set:
                if (mTxtvDialogTitle.getText().equals("设置声音")){
                    mAppConfig.setSoundFast(isChecked);
                } else {
                    mAppConfig.setSharkFast(isChecked);
                }
                break;
            case R.id.switch_slow_set:
                if (mTxtvDialogTitle.getText().equals("设置声音")){
                    mAppConfig.setSoundSlow(isChecked);
                } else {
                    mAppConfig.setSharkSlow(isChecked);
                }
                break;
            case R.id.switch_stop_set:
                if (mTxtvDialogTitle.getText().equals("设置声音")){
                    mAppConfig.setSoundStop(isChecked);
                } else {
                    mAppConfig.setSharkStop(isChecked);
                }
                break;
            case R.id.switch_low_power_set:
                if (mTxtvDialogTitle.getText().equals("设置声音")){
                    mAppConfig.setSoundLowPower(isChecked);
                } else {
                    mAppConfig.setSharkLowPower(isChecked);
                }
                break;
            default:
                break;
        }
    }
}
