package com.daytoday.business.dailydelivery.MainHomeScreen.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Bussiness implements Serializable {

    @SerializedName("bussid")
    @Expose
    private String bussid;
    @SerializedName("bussuserid")
    @Expose
    private String bussuserid;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Phoneno")
    @Expose
    private String phoneno;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Price")
    @Expose
    private int price;
    @SerializedName("DOrM")
    @Expose
    private String dOrM;
    @SerializedName("Payment")
    @Expose
    private String payment;
    @SerializedName("Imgurl")
    @Expose
    private String imgurl;
    @SerializedName("NoOfCust")
    @Expose
    private int noOfCust;
    @SerializedName("TotCan")
    @Expose
    private int totCan;
    @SerializedName("TotEarn")
    @Expose
    private int totEarn;
    @SerializedName("TotPen")
    @Expose
    private int totPen;

    public String getBussid() {
        return bussid;
    }

    public void setBussid(String bussid) {
        this.bussid = bussid;
    }

    public String getBussuserid() {
        return bussuserid;
    }

    public void setBussuserid(String bussuserid) {
        this.bussuserid = bussuserid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getdOrM() {
        return dOrM;
    }

    public void setdOrM(String dOrM) {
        this.dOrM = dOrM;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getNoOfCust() {
        return noOfCust;
    }

    public void setNoOfCust(int noOfCust) {
        this.noOfCust = noOfCust;
    }

    public int getTotCan() {
        return totCan;
    }

    public void setTotCan(int totCan) {
        this.totCan = totCan;
    }

    public int getTotEarn() {
        return totEarn;
    }

    public void setTotEarn(int totEarn) {
        this.totEarn = totEarn;
    }

    public int getTotPen() {
        return totPen;
    }

    public void setTotPen(int totPen) {
        this.totPen = totPen;
    }
}
