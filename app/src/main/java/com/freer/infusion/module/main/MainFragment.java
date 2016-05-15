package com.freer.infusion.module.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.freer.infusion.R;
import com.freer.infusion.base.BaseFragment;
import com.freer.infusion.entity.SocketEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2016/5/15.
 */
public class MainFragment extends BaseFragment {

    private List<SocketEntity> mDataList = new ArrayList<SocketEntity>(); // 数据列表
    private MainListAdapter mMainListAdapter;

    public void setData(List<SocketEntity> dataList) {
        this.mDataList = dataList;
        mMainListAdapter.setData(mDataList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListViewCompat listViewCompat = (ListViewCompat) rootView.findViewById(R.id.list);
        listViewCompat.setAdapter(mMainListAdapter = new MainListAdapter(getActivity()));

        listViewCompat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return rootView;
    }
}
