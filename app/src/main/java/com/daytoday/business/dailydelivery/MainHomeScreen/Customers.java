package com.daytoday.business.dailydelivery.MainHomeScreen;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Customers {
    String name;
    String adress;
    String Id;
    String photoUrl;
    String phoneNo;

    public Customers(String name, String adress, String id, String photoUrl, String phoneNo) {
        this.name = name;
        this.adress = adress;
        Id = id;
        this.photoUrl = photoUrl;
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
