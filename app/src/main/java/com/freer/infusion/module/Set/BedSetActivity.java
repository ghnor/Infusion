package com.freer.infusion.module.Set;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.freer.infusion.R;
import com.freer.infusion.base.BaseActivity;

public class BedSetActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private BedFragmentAdapter mBedFragmentAdapter;
    private MyBedFragment mMyBedFragment;
    private FollowFragment mFollowFragment;

    private Toolbar mToolbarBed;
    private TabLayout mTablayoutBed;
    private ViewPager mViewpagerBed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bed_set);

        mContext = this;

        initView();
        initEvent();
    }

    private void initView() {
        mToolbarBed = (Toolbar) findViewById(R.id.toolbar_bed);
        setSupportActionBar(mToolbarBed);

        mTablayoutBed = (TabLayout) findViewById(R.id.tablayout_bed);
        mViewpagerBed = (ViewPager) findViewById(R.id.viewpager_bed);

        mMyBedFragment = new MyBedFragment();
        mFollowFragment = new FollowFragment();
        mBedFragmentAdapter = new BedFragmentAdapter(getSupportFragmentManager());
        mBedFragmentAdapter.addFragment(mMyBedFragment, "我的床位");
        mBedFragmentAdapter.addFragment(mFollowFragment, "关注床位");

        mViewpagerBed.setAdapter(mBedFragmentAdapter);
        mTablayoutBed.setupWithViewPager(mViewpagerBed);

    }

    private void initEvent() {

    }

    @Override
    public void onClick(View v) {

    }
}
