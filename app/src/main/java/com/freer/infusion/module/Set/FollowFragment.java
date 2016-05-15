package com.freer.infusion.module.Set;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.freer.infusion.R;
import com.freer.infusion.base.BaseFragment;
import com.freer.infusion.config.AppConfig;
import com.freer.infusion.util.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 2172980000773 on 2016/5/11.
 */
public class FollowFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private Context mContext;
    private ArrayList<CheckBox> mCheckList; // 存放所有的Check对象

    private View mRootView;
    private LinearLayout mLinearMyBed;
    private Button mBtnOk; // 确定按钮
    private Button mBtnAll; // 全选按钮
    private Button mBtnCancel; // 全不选按钮

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity();

        mRootView = inflater.inflate(R.layout.fragment_follow_bed, container, false);

        initView();
        initEvent();

        return mRootView;
    }

    private void initView() {

        mCheckList = new ArrayList<>();
        HashMap<String, String> hashMap = AppConfig.getInstance().getFollowBed();
        if (hashMap == null){
            hashMap = new HashMap<>();
        }

        mLinearMyBed = (LinearLayout) mRootView.findViewById(R.id.linear_follow_bed);
        mBtnOk = (Button) mRootView.findViewById(R.id.btn_follow_ok);
        mBtnAll = (Button) mRootView.findViewById(R.id.btn_follow_all);
        mBtnCancel = (Button) mRootView.findViewById(R.id.btn_follow_cancel);

        /* 循环创建100个床位 */
        for (int i = 0; i < 20; i++) {
            LinearLayout linearLayout = new LinearLayout(mContext);
            linearLayout.setGravity(Gravity.CENTER_HORIZONTAL); // 设置按钮居中显示
            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10, 10, 10, 10);
            for (int j = 0; j < 5; j++) {
                CheckBox box = new CheckBox(mContext); // 创建按钮对象
                box.setLayoutParams(lp); // 设置checkBox之间的间距
                box.setWidth(CommonUtil.dip2px(mContext, 60)); // 设置CheckBox的宽度
                box.setHeight(CommonUtil.dip2px(mContext, 60)); // 设置CheckBox的高度
                box.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT)); // 设置选择按钮为透明
                box.setId(i * 5 + j + 1); // 设置按钮的id
                box.setText("" + (i * 5 + j + 1)); // 设置按钮的字
                box.setBackgroundResource(R.drawable.select_round_btn_blue);
                box.setOnCheckedChangeListener(this); // 选择按钮
                box.setChecked(hashMap.containsKey("" + (i * 5 + j + 1))); // 根据本地存储的数据来显示CheckBox是否选择
                linearLayout.addView(box);
                mCheckList.add(box);
            }
            mLinearMyBed.addView(linearLayout, linearParams);
        }
    }

    private void initEvent() {
        mBtnOk.setOnClickListener(this);
        mBtnAll.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_follow_ok:
                // 确定改变
                AppConfig.getInstance().setFollowBed(CommonUtil.saveAsHashMap(mCheckList)); // 将选择的床位保存在本地
                break;
            case R.id.btn_follow_all:
                // 选择全部
                if (mCheckList != null){
                    for (CheckBox checkBox: mCheckList) {
                        checkBox.setChecked(true);
                    }
                }

                break;
            case R.id.btn_follow_cancel:
                // 选择取消
                if (mCheckList != null){
                    for (CheckBox checkBox: mCheckList) {
                        checkBox.setChecked(false);
                    }
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
