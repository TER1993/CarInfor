package com.speedata.carinfor.db.bean;

import com.elsw.base.db.orm.annotation.Column;
import com.elsw.base.db.orm.annotation.Table;
import com.speedata.libutils.excel.Excel;

/**
 * Created by Administrator on 2017/8/22.
 */
@Table(name = "BaseInfor")
public  class BaseInfor { //导入excel表格内容


    @Excel(ignore = false, name = "卡号EPC")
    @Column(name = "ACardEPC")
    private String ACardEPC; //卡号EPC

    @Excel(ignore = false, name = "车架号")
    @Column(name = "BFrameNumber")
    private String BFrameNumber; //车架号

    @Excel(ignore = false, name = "品牌")
    @Column(name = "CBrand")
    private String CBrand; //品牌

    @Excel(ignore = false, name = "颜色")
    @Column(name = "DColor")
    private String DColor; //颜色

    @Excel(ignore = false, name = "公里数")
    @Column(name = "EKilometers")
    private String EKilometers; //公里数

    @Excel(ignore = false, name = "存车位置")
    @Column(name = "FParkingLocation")
    private String FParkingLocation; //存车位置

    @Excel(ignore = false, name = "进场时间")
    @Column(name = "GApproachTime")
    private String GApproachTime; //进场时间


    public String getACardEPC() {
        return ACardEPC;
    }

    public void setACardEPC(String ACardEPC) {
        this.ACardEPC = ACardEPC;
    }

    public String getBFrameNumber() {
        return BFrameNumber;
    }

    public void setBFrameNumber(String BFrameNumber) {
        this.BFrameNumber = BFrameNumber;
    }

    public String getCBrand() {
        return CBrand;
    }

    public void setCBrand(String CBrand) {
        this.CBrand = CBrand;
    }

    public String getDColor() {
        return DColor;
    }

    public void setDColor(String DColor) {
        this.DColor = DColor;
    }

    public String getEKilometers() {
        return EKilometers;
    }

    public void setEKilometers(String EKilometers) {
        this.EKilometers = EKilometers;
    }

    public String getFParkingLocation() {
        return FParkingLocation;
    }

    public void setFParkingLocation(String FParkingLocation) {
        this.FParkingLocation = FParkingLocation;
    }

    public String getGApproachTime() {
        return GApproachTime;
    }

    public void setGApproachTime(String GApproachTime) {
        this.GApproachTime = GApproachTime;
    }

    @Override
    public String toString() {
        return "BaseInfor{" +
                "ACardEPC='" + ACardEPC + '\'' +
                ", BFrameNumber='" + BFrameNumber + '\'' +
                ", CBrand='" + CBrand + '\'' +
                ", DColor='" + DColor + '\'' +
                ", EKilometers='" + EKilometers + '\'' +
                ", FParkingLocation='" + FParkingLocation + '\'' +
                ", GApproachTime='" + GApproachTime + '\'' +
                '}';
    }
}
