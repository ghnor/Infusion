package com.freer.infusion.entity;

/**
 * Created by 2172980000774 on 2016/5/13.
 */
public class SocketEntity {
    public int LogId = 0; //主键
    public String RFId; //中继id
    public String UxId; //设备编号
    public String UxName; //设备自定义名称
    public int BedId; //床位编号
    public int CurrSpeed; //动态滴速
    public int TopLimitSpeed; //上限滴速
    public int LowLimitSpeed; //下限滴速
    public int DropAmount; //液滴总数
    public int WorkingState; //设备工作状态，显示颜色与Ux_DicColor对应
    public int WarnProcess; //设备运行报警进程
    public int RealProcess; //设备运行真实进程
    public int ClientAction; //用户设定的总容量，这个数据从客户端设备获取
    public int State; //数据状态；1：正常
    public String Key; //
    public int LastRealProcess; //非正常撤机，5分钟内又挂上去时，记录撤机时实际已输液量
    public int StopWarningCount;
    public int LimitWarningCount;
    public int SettingSpeed; //设定滴速
    public int BatteryCapacity; //低电量
    public String WarningMSG; //设备异常，不当操作等异常信息提醒
    public String TempBedID; //
    public int IsWorkingStartUx; //

    @Override
    public String toString() {
        return "{" +
                "LogId=" + LogId +
                ", RFId='" + RFId + '\'' +
                ", UxId='" + UxId + '\'' +
                ", UxName='" + UxName + '\'' +
                ", BedId=" + BedId +
                ", CurrSpeed=" + CurrSpeed +
                ", TopLimitSpeed=" + TopLimitSpeed +
                ", LowLimitSpeed=" + LowLimitSpeed +
                ", DropAmount=" + DropAmount +
                ", WorkingState=" + WorkingState +
                ", WarnProcess=" + WarnProcess +
                ", RealProcess=" + RealProcess +
                ", ClientAction=" + ClientAction +
                ", State=" + State +
                ", Key='" + Key + '\'' +
                ", LastRealProcess=" + LastRealProcess +
                ", StopWarningCount=" + StopWarningCount +
                ", LimitWarningCount=" + LimitWarningCount +
                ", SettingSpeed=" + SettingSpeed +
                ", BatteryCapacity=" + BatteryCapacity +
                ", WarningMSG='" + WarningMSG + '\'' +
                ", TempBedID='" + TempBedID + '\'' +
                ", IsWorkingStartUx=" + IsWorkingStartUx +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof SocketEntity)) return false;

        return ((SocketEntity) o).UxName.equals(SocketEntity.this.UxName);
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(SocketEntity.this.UxName, 16);
    }
}
