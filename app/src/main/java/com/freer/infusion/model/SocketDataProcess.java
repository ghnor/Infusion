package com.freer.infusion.model;

import com.freer.infusion.entity.DataEntity;
import com.freer.infusion.entity.SocketEntity;
import com.freer.infusion.util.JsonUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by zhang on 2016/5/14.
 */
public class SocketDataProcess {

    public SocketDataProcess() {
        System.out.println("实例化SocketDataProcess");
    }

    private List<SocketEntity> mAllList = new ArrayList<>();
    private List<SocketEntity> mFollowList = new ArrayList<>();

    public void processData(String message) {

        mAllList.clear();
        mFollowList.clear();

        DataEntity dataEntity = JsonUtils.fromJson(message, DataEntity.class);
        if (dataEntity == null || dataEntity.d == null) return;
        for(int index = 0; index < dataEntity.d.size(); index++) {
            SocketEntity socketEntity = dataEntity.d.get(index);
            if(socketEntity.BedId == TEST_BED_FOLLOW) {
                mAllList.add(socketEntity);
            }
            if (socketEntity.BedId == TEST_BED_VIOP) {
                mFollowList.add(socketEntity);
            }
        }
    }

    /**
     * 获取全部床位的数据
     * @return
     */
    public List<SocketEntity> getAllBed() {
        return mAllList;
    }

    /**
     * 获取关注床位的数据
     * @return
     */
    public List<SocketEntity> getFollowBed() {
        return mFollowList;
    }

    public static final int WORK_NO = 0; //不用，白色
    public static final int WORK_BEGIN = 1; //进入工作状态，绿色
    public static final int WORK_NORMAL = 2; //正常工作，绿色
    public static final int WORK_FAST = 3; //过快，黄色
    public static final int WORK_SLOW = 4; //过慢，蓝色
    public static final int WORK_STOP = 5; //停止，红色 包含设备故障
    public static final int WORK_POWER = 6; //低电，紫色
    public static final int WORK_PAUSE = 7; //输液暂停，紫色
    public static final int WORK_ERROR = 10; //异常关机


    public static final int TEST_BED_FOLLOW = 28;
    public static final int TEST_BED_VIOP = 25;

}
