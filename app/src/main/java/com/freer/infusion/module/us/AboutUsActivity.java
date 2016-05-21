package com.freer.infusion.module.us;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.freer.infusion.R;
import com.freer.infusion.base.BaseActivity;

/**
 * Created by 2172980000774 on 2016/5/18.
 */
public class AboutUsActivity extends BaseActivity implements View.OnClickListener{

    private Context mContext;

    private Toolbar mToolbarAbout; // 标题栏
    private Button mBtnCallPhone; // 拨打电话按钮

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        mContext = this;

        initView();
        initEvent();
    }

    private void initView() {
        mToolbarAbout = (Toolbar) findViewById(R.id.toolbar_about);
        mToolbarAbout.setTitle(R.string.string_title_about);
        setSupportActionBar(mToolbarAbout);

        mBtnCallPhone = (Button) findViewById(R.id.btn_call_phone);
    }

    private void initEvent() {
        mBtnCallPhone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_call_phone:
                // TODO 拨打电话
                dialPhoneNumber("0571-88952736");
                break;
            default:
                break;
        }

    }

    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
