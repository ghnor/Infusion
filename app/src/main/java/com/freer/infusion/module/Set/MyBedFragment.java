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
import com.freer.infusion.util.CommenUtil;

/**
 * Created by 2172980000773 on 2016/5/11.
 */
public class MyBedFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private Context mContext;

    private View mRootView;
    private LinearLayout mLinearMyBed;
    private Button mBtnAll;
    private Button mBtnCancel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity();

        mRootView = inflater.inflate(R.layout.fragment_my_bed, container, false);

        initView();
        initEvent();

        return mRootView;
    }

    private void initView() {

        mLinearMyBed = (LinearLayout) mRootView.findViewById(R.id.linear_my_bed);
        mBtnAll = (Button) mRootView.findViewById(R.id.btn_bed_all);
        mBtnCancel = (Button) mRootView.findViewById(R.id.btn_bed_cancel);

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
                box.setWidth(CommenUtil.dip2px(mContext, 60)); // 设置CheckBox的宽度
                box.setHeight(CommenUtil.dip2px(mContext, 60)); // 设置CheckBox的高度
                box.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT)); // 设置选择按钮为透明
                box.setId(i * 5 + j + 1); // 设置按钮的id
                box.setText("" + (i * 5 + j + 1)); // 设置按钮的字
                box.setBackground(getResources().getDrawable(R.drawable.select_round_btn_blue));
                box.setOnCheckedChangeListener(this); // 选择按钮
                linearLayout.addView(box);
            }
            mLinearMyBed.addView(linearLayout, linearParams);
        }
    }

    private void initEvent() {
        mBtnAll.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
