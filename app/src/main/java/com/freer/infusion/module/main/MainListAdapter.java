package com.freer.infusion.module.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.freer.infusion.R;
import com.freer.infusion.entity.SocketEntity;
import com.freer.infusion.model.SocketDataProcess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhang on 2016/5/15.
 */
public class MainListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private List<SocketEntity> mDataList;
    private Map<String, SocketEntity> mDataMap;

    public MainListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);

        mDataList = new ArrayList<SocketEntity>();
        mDataMap = new HashMap<String, SocketEntity>();
    }

    public void setData(List<SocketEntity> dataList) {
        for(int index = 0; index < dataList.size(); index++) {
            SocketEntity socketEntity = dataList.get(index);
            mDataMap.put(socketEntity.UxName, socketEntity);
        }

        //先清空一次list
        mDataList.clear();
        for (SocketEntity socketEntity : mDataMap.values()) {
            mDataList.add(socketEntity);
        }

        notifyDataSetChanged();
    }

    private Drawable getProgressDrawable(int workState) {
        switch (workState) {
            case SocketDataProcess.WORK_NO:
                return ContextCompat.getDrawable(mContext, R.drawable.progressbar_green);
            case SocketDataProcess.WORK_BEGIN:
                return ContextCompat.getDrawable(mContext, R.drawable.progressbar_green);
            case SocketDataProcess.WORK_NORMAL:
                return ContextCompat.getDrawable(mContext, R.drawable.progressbar_green);
            case SocketDataProcess.WORK_FAST:
                return ContextCompat.getDrawable(mContext, R.drawable.progressbar_yellow);
            case SocketDataProcess.WORK_SLOW:
                return ContextCompat.getDrawable(mContext, R.drawable.progressbar_blue);
            case SocketDataProcess.WORK_STOP:
                return ContextCompat.getDrawable(mContext, R.drawable.progressbar_purple);
            case SocketDataProcess.WORK_POWER:
                return ContextCompat.getDrawable(mContext, R.drawable.progressbar_red);
            case SocketDataProcess.WORK_PAUSE:
                return ContextCompat.getDrawable(mContext, R.drawable.progressbar_purple);
            case SocketDataProcess.WORK_ERROR:
                return ContextCompat.getDrawable(mContext, R.drawable.progressbar_red);
            default:
                return ContextCompat.getDrawable(mContext, R.drawable.progressbar_green);
        }
    }

    /**
     * 根据不同工作状态，设置显示颜色
     * @param workState
     * @return
     */
    private int getColor(int workState) {
        switch (workState) {
            case SocketDataProcess.WORK_NO:
                return ContextCompat.getColor(mContext, R.color.white);
            case SocketDataProcess.WORK_BEGIN:
                return ContextCompat.getColor(mContext, R.color.green);
            case SocketDataProcess.WORK_NORMAL:
                return ContextCompat.getColor(mContext, R.color.green);
            case SocketDataProcess.WORK_FAST:
                return ContextCompat.getColor(mContext, R.color.yellow);
            case SocketDataProcess.WORK_SLOW:
                return ContextCompat.getColor(mContext, R.color.blue);
            case SocketDataProcess.WORK_STOP:
                return ContextCompat.getColor(mContext, R.color.purple);
            case SocketDataProcess.WORK_POWER:
                return ContextCompat.getColor(mContext, R.color.red);
            case SocketDataProcess.WORK_PAUSE:
                return ContextCompat.getColor(mContext, R.color.purple);
            case SocketDataProcess.WORK_ERROR:
                return ContextCompat.getColor(mContext, R.color.red);
            default:
                return ContextCompat.getColor(mContext, R.color.green);
        }
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.item_main, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SocketEntity data = mDataList.get(position);
        holder.mView.setBackgroundColor(getColor(data.WorkingState));
        holder.mBedNum.setText(String.valueOf(data.BedId));
        holder.mSpeed.setText(String.valueOf(data.CurrSpeed));
        holder.mSpeedLower.setText(String.valueOf(data.LowLimitSpeed));
        holder.mSpeedUpper.setText(String.valueOf(data.TopLimitSpeed));
        holder.mTotalNum.setText(String.valueOf(data.ClientAction));
        holder.mProgress.setProgress(data.RealProcess);
        holder.mProgress.setProgressDrawable(getProgressDrawable(data.WorkingState));

        return convertView;
    }

    public static class ViewHolder {

        View mView;
        TextView mBedNum;
        TextView mTotalNum;
        TextView mSpeed;
        TextView mSpeedLower;
        TextView mSpeedUpper;
        ProgressBar mProgress;

        public ViewHolder(View itemView) {
            mView = itemView.findViewById(R.id.view);
            mBedNum = (TextView) itemView.findViewById(R.id.bed_number);
            mTotalNum = (TextView) itemView.findViewById(R.id.total_number);
            mSpeed = (TextView) itemView.findViewById(R.id.speed);
            mSpeedLower = (TextView) itemView.findViewById(R.id.speed_lower);
            mSpeedUpper = (TextView) itemView.findViewById(R.id.speed_upper);
            mProgress = (ProgressBar) itemView.findViewById(R.id.progress);
        }

    }
}
