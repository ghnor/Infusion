package com.freer.infusion.module.main;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.freer.infusion.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 2172980000774 on 2016/5/10.
 */
public class MainRvAdapter extends RecyclerView.Adapter<MainViewHolder> {

    private List<String> mDataList;

    public MainRvAdapter() {}

    public void setItem(List<String> dataList) {
        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.item_main, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        Log.d("MainRvAdapter", String.valueOf(mDataList.size()));
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        }
        return mDataList.size();
    }
}
