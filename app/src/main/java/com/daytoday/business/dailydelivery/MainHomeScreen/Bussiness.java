package com.daytoday.business.dailydelivery.MainHomeScreen;

import android.os.Parcel;
import android.os.Parcelable;

public class Bussiness implements Parcelable {

    private String productName, tarrif;
    private String price;
    private String earning;
    private String image;
    private String cust_cout;
    private String pay_mode;

    public Bussiness(String productName, String tarrif, String price, String earning, String image, String cust_cout, String pay_mode) {
        this.productName = productName;
        this.tarrif = tarrif;
        this.price = price;
        this.earning = earning;
        this.image = image;
        this.cust_cout = cust_cout;
        this.pay_mode = pay_mode;
    }

    protected Bussiness(Parcel in) {
        productName = in.readString();
        tarrif = in.readString();
        price = in.readString();
        earning = in.readString();
        image = in.readString();
        cust_cout = in.readString();
        pay_mode = in.readString();
    }

    public static final Creator<Bussiness> CREATOR = new Creator<Bussiness>() {
        @Override
        public Bussiness createFromParcel(Parcel in) {
            return new Bussiness(in);
        }

        @Override
        public Bussiness[] newArray(int size) {
            return new Bussiness[size];
        }
    };

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTarrif() {
        return tarrif;
    }

    public void setTarrif(String tarrif) {
        this.tarrif = tarrif;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEarning() {
        return earning;
    }

    public void setEarning(String earning) {
        this.earning = earning;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCust_cout() {
        return cust_cout;
    }

    public void setCust_cout(String cust_cout) {
        this.cust_cout = cust_cout;
    }

    public String getPay_mode() {
        return pay_mode;
    }

    public void setPay_mode(String pay_mode) {
        this.pay_mode = pay_mode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productName);
        dest.writeString(tarrif);
        dest.writeString(price);
        dest.writeString(earning);
        dest.writeString(image);
        dest.writeString(cust_cout);
        dest.writeString(pay_mode);
    }
}
