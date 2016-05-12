package com.freer.infusion.module.Set;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freer.infusion.R;
import com.freer.infusion.base.BaseFragment;

/**
 * Created by 2172980000773 on 2016/5/11.
 */
public class FollowFragment extends BaseFragment {

    private Context mContext;

    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity();

        mRootView = inflater.inflate(R.layout.fragment_follow_bed, container, false);

        return mRootView;
    }
}
