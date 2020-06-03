package com.daytoday.business.dailydelivery.MainHomeScreen.Model;

public class EmployeeInfo {
    String name;
    String id;
    String phoneNo;
    String photourl;

    public EmployeeInfo(String name, String id, String phoneNo, String photourl) {
        this.name = name;
        this.id = id;
        this.phoneNo = phoneNo;
        this.photourl = photourl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }
}
