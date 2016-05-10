package com.freer.infusion.module.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freer.infusion.R;
import com.freer.infusion.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 2172980000774 on 2016/5/10.
 */
public class AllFragment extends BaseFragment {
    private List<String> mDataList = new ArrayList<String>(); // 数据列表
    private MainRvAdapter mMainRvAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mMainRvAdapter = new MainRvAdapter());

        mDataList.add("");
        mMainRvAdapter.setItem(mDataList);

        return rootView;
    }
}
