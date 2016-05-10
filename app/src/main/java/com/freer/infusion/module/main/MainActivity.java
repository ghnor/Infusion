package com.freer.infusion.module.main;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.freer.infusion.R;
import com.freer.infusion.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private FllowFragment mFllowFragment;
    private AllFragment mAllFragment;

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TabFragmentAdapter mTabFragmentAdapter;

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_setting) {
            return true;
        } else if (id == R.id.menu_aboutus) {

        } else if (id == R.id.menu_quit) {

        }

        return super.onOptionsItemSelected(item);
    }
}
