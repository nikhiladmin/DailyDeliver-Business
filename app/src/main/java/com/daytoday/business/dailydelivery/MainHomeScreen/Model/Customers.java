package com.daytoday.business.dailydelivery.MainHomeScreen.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Customers implements Serializable {
    @SerializedName("UniqueId")
    @Expose
    private String bussCustID;
    @SerializedName("BussId")
    @Expose
    private String bussId;
    @SerializedName("CustId")
    @Expose
    private String custId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("CustToken")
    @Expose
    private String custToken;

    public String getBussCustID() {
        return bussCustID;
    }

    public void setBussCustID(String bussCustID) {
        this.bussCustID = bussCustID;
    }

    public String getBussId() {
        return bussId;
    }

    public void setBussId(String bussId) {
        this.bussId = bussId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustToken() {
        return custToken;
    }

    public void setCustToken(String custToken) {
        this.custToken = custToken;
    }
}
