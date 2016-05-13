package com.freer.infusion.module.main;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freer.infusion.R;
import com.freer.infusion.entity.DataEntity;
import com.freer.infusion.entity.SocketEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 2172980000774 on 2016/5/10.
 */
public class MainRvAdapter extends RecyclerView.Adapter<MainRvViewHolder> {

    public interface OnRecyclerItemClickListener {
        public void onItemClick(int position, SocketEntity data);
    }

    public MainRvAdapter() {
        mDataList = new ArrayList<SocketEntity>();
    }

    private List<SocketEntity> mDataList;

    private OnRecyclerItemClickListener mOnRecyclerItemClickListener;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.mOnRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    public void setItem(List<SocketEntity> dataList) {
//        for (SocketEntity dataLocal : mDataList) {
//            for (SocketEntity dataServer : dataList){
//                if (dataServer.UxName.equals(dataLocal.UxName)) {
//                    mDataList.set(mDataList.indexOf(dataLocal), dataServer);
//                } else {
//                    mDataList.add(dataServer);
//                }
//            }
//        }
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public MainRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MainRvViewHolder mainRvViewHolder = new MainRvViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_main, parent, false));
        return mainRvViewHolder;
    }

    @Override
    public void onBindViewHolder(MainRvViewHolder holder, final int position) {
        Log.d("MainRvAdapter", String.valueOf(mDataList.size()));
        holder.itemView.setBackgroundResource(R.drawable.item_bg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRecyclerItemClickListener.onItemClick(position, mDataList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        }
        return mDataList.size();
    }
}
