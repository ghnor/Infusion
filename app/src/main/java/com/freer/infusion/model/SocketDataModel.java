package com.freer.infusion.model;

import com.freer.infusion.entity.SocketEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 2172980000774 on 2016/5/17.
 */
public class SocketDataModel {

    private List<SocketEntity> mDataList;
    private Map<String, SocketEntity> mDataMap;

    public SocketDataModel() {
        mDataList = new ArrayList<>();
        mDataMap = new HashMap<>();
    }

    public void setData(List<SocketEntity> dataList) {
        for(int index = 0; index < dataList.size(); index++) {
            SocketEntity socketEntity = dataList.get(index);
            mDataMap.put(socketEntity.UxName, socketEntity);
        }
    }

    /**
     * 更新数据
     * @param data
     */
    public void setData(SocketEntity data) {
        mDataMap.put(data.UxName, data);
    }

    /**
     * 更新数据，不添加新数据
     * @param data
     */
    public void setDataNoAdd(SocketEntity data) {
        //判断当前数据中如果已经含有这段数据，则更新
        if (mDataMap.containsKey(data.UxName)) {
            mDataMap.put(data.UxName, data);
        }
    }

    public List<SocketEntity> getData() {
        //先清空一次list
        mDataList.clear();
        for (Map.Entry<String, SocketEntity> entry : mDataMap.entrySet()) {
            if (entry.getValue().WorkingState == SocketDataProcess.WORK_NO) {
                mDataMap.remove(entry.getKey());
            } else {
                mDataList.add(entry.getValue());
            }
        }
        return mDataList;
    }
}
