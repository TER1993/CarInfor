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
    @Column(name = "CardEPC")
    private String CardEPC; //卡号EPC

    @Excel(ignore = false, name = "车架号")
    @Column(name = "FrameNumber")
    private String FrameNumber; //车架号

    @Excel(ignore = false, name = "品牌")
    @Column(name = "Brand")
    private String Brand; //品牌

    @Excel(ignore = false, name = "颜色")
    @Column(name = "Color")
    private String Color; //颜色

    @Excel(ignore = false, name = "公里数")
    @Column(name = "Kilometers")
    private String Kilometers; //公里数

    @Excel(ignore = false, name = "存车位置")
    @Column(name = "ParkingLocation")
    private String ParkingLocation; //存车位置

    @Excel(ignore = false, name = "进场时间")
    @Column(name = "ApproachTime")
    private String ApproachTime; //进场时间



    public String getCardEPC() {
        return CardEPC;
    }

    public void setCardEPC(String cardEPC) {
        CardEPC = cardEPC;
    }

    public String getFrameNumber() {
        return FrameNumber;
    }

    public void setFrameNumber(String frameNumber) {
        FrameNumber = frameNumber;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getKilometers() {
        return Kilometers;
    }

    public void setKilometers(String kilometers) {
        Kilometers = kilometers;
    }

    public String getParkingLocation() {
        return ParkingLocation;
    }

    public void setParkingLocation(String parkingLocation) {
        ParkingLocation = parkingLocation;
    }

    public String getApproachTime() {
        return ApproachTime;
    }

    public void setApproachTime(String approachTime) {
        ApproachTime = approachTime;
    }




    @Override
    public String toString() {
        return "BaseInfor{" +
                "CardEPC='" + CardEPC + '\'' +
                ", FrameNumber='" + FrameNumber + '\'' +
                ", Brand='" + Brand + '\'' +
                ", Color='" + Color + '\'' +
                ", Kilometers='" + Kilometers + '\'' +
                ", ParkingLocation='" + ParkingLocation + '\'' +
                ", ApproachTime='" + ApproachTime + '\'' +
                '}';
    }

}
