package com.daytoday.business.dailydelivery.MainHomeScreen.Model;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Customers {
    @SerializedName("UniqueId")
    @Expose
    private String uniqueId;
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
    @SerializedName("profilepic")
    @Expose
    String custProfilepic;

    public String getCustProfilepic() {
        return custProfilepic;
    }

    public void setCustProfilepic(String custProfilepic) {
        this.custProfilepic = custProfilepic;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
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
}
