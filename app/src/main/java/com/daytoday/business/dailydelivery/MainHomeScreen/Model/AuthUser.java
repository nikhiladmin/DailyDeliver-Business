
package com.daytoday.business.dailydelivery.MainHomeScreen.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthUser {

    @SerializedName("bussuserId")
    @Expose
    private String bussuserId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phoneno")
    @Expose
    private String phoneno;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("token")
    @Expose
    private String token;

    public String getBussuserId() {
        return bussuserId;
    }

    public void setBussuserId(String bussuserId) {
        this.bussuserId = bussuserId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
