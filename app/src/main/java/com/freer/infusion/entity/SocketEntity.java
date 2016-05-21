package com.freer.infusion.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 2172980000774 on 2016/5/13.
 */
public class SocketEntity implements Parcelable {
    public int LogId; //主键
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

    protected SocketEntity(Parcel in) {
        LogId = in.readInt();
        RFId = in.readString();
        UxId = in.readString();
        UxName = in.readString();
        BedId = in.readInt();
        CurrSpeed = in.readInt();
        TopLimitSpeed = in.readInt();
        LowLimitSpeed = in.readInt();
        DropAmount = in.readInt();
        WorkingState = in.readInt();
        WarnProcess = in.readInt();
        RealProcess = in.readInt();
        ClientAction = in.readInt();
        State = in.readInt();
        Key = in.readString();
        LastRealProcess = in.readInt();
        StopWarningCount = in.readInt();
        LimitWarningCount = in.readInt();
        SettingSpeed = in.readInt();
        BatteryCapacity = in.readInt();
        WarningMSG = in.readString();
        TempBedID = in.readString();
        IsWorkingStartUx = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(LogId);
        dest.writeString(RFId);
        dest.writeString(UxId);
        dest.writeString(UxName);
        dest.writeInt(BedId);
        dest.writeInt(CurrSpeed);
        dest.writeInt(TopLimitSpeed);
        dest.writeInt(LowLimitSpeed);
        dest.writeInt(DropAmount);
        dest.writeInt(WorkingState);
        dest.writeInt(WarnProcess);
        dest.writeInt(RealProcess);
        dest.writeInt(ClientAction);
        dest.writeInt(State);
        dest.writeString(Key);
        dest.writeInt(LastRealProcess);
        dest.writeInt(StopWarningCount);
        dest.writeInt(LimitWarningCount);
        dest.writeInt(SettingSpeed);
        dest.writeInt(BatteryCapacity);
        dest.writeString(WarningMSG);
        dest.writeString(TempBedID);
        dest.writeInt(IsWorkingStartUx);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SocketEntity> CREATOR = new Creator<SocketEntity>() {
        @Override
        public SocketEntity createFromParcel(Parcel in) {
            return new SocketEntity(in);
        }

        @Override
        public SocketEntity[] newArray(int size) {
            return new SocketEntity[size];
        }
    };

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
