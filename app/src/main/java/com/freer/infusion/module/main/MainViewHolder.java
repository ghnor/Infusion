package com.freer.infusion.module.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.freer.infusion.R;

/**
 * Created by 2172980000774 on 2016/5/10.
 */
public class MainViewHolder extends RecyclerView.ViewHolder {

    View mView;
    TextView mBedNum;
    TextView mTotalNum;
    TextView mSpeed;
    TextView mSpeedLower;
    TextView mSpeedUpper;

    public MainViewHolder(View itemView) {
        super(itemView);
        mView = itemView.findViewById(R.id.view);
        mBedNum = (TextView) itemView.findViewById(R.id.bed_number);
        mTotalNum = (TextView) itemView.findViewById(R.id.total_number);
        mSpeed = (TextView) itemView.findViewById(R.id.speed);
        mSpeedLower = (TextView) itemView.findViewById(R.id.speed_lower);
        mSpeedUpper = (TextView) itemView.findViewById(R.id.speed_upper);
    }
}
