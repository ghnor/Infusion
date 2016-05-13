package com.freer.infusion.module.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.freer.infusion.R;
import com.freer.infusion.base.BaseActivity;
import com.freer.infusion.entity.DataEntity;
import com.freer.infusion.entity.SocketEntity;
import com.freer.infusion.module.Set.SetActivity;
import com.freer.infusion.module.SocketTestActivity;
import com.freer.infusion.util.DialogManger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private FllowFragment mFllowFragment; //关注床位界面
    private AllFragment mAllFragment; //所有床位界面

    private Toolbar mToolbar; //标题栏
    private TabLayout mTabLayout; //标签页tip
    private ViewPager mViewPager; //标签页
    private TabFragmentAdapter mTabFragmentAdapter; //标签页适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 标题栏
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mFllowFragment = new FllowFragment();
        mAllFragment = new AllFragment();

        mTabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager());
        mTabFragmentAdapter.addFragment(mFllowFragment, "关注床位");
        mTabFragmentAdapter.addFragment(mAllFragment, "全部床位");

        mViewPager.setAdapter(mTabFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
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
//            DataEntity dataEntity = new DataEntity();
//            dataEntity.cate = 0;
//            SocketEntity socketEntity = new SocketEntity();
//            socketEntity.LogId = 0;
//            List<SocketEntity> list = new ArrayList<SocketEntity>();
//            list.add(socketEntity);
//            list.add(socketEntity);
//            Log.e("实体类输出结果", list.toString());
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
}
