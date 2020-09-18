package com.daytoday.business.dailydelivery.MainHomeScreen.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bussiness implements Parcelable {

    @SerializedName("bussid")
    @Expose
    private Integer bussid;
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
    private Integer price;
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
    private Integer noOfCust;
    @SerializedName("TotCan")
    @Expose
    private Integer totCan;
    @SerializedName("TotEarn")
    @Expose
    private Integer totEarn;
    @SerializedName("TotPen")
    @Expose
    private Integer totPen;

    protected Bussiness(Parcel in) {
        if (in.readByte() == 0) {
            bussid = null;
        } else {
            bussid = in.readInt();
        }
        bussuserid = in.readString();
        name = in.readString();
        phoneno = in.readString();
        address = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readInt();
        }
        dOrM = in.readString();
        payment = in.readString();
        imgurl = in.readString();
        if (in.readByte() == 0) {
            noOfCust = null;
        } else {
            noOfCust = in.readInt();
        }
        if (in.readByte() == 0) {
            totCan = null;
        } else {
            totCan = in.readInt();
        }
        if (in.readByte() == 0) {
            totEarn = null;
        } else {
            totEarn = in.readInt();
        }
        if (in.readByte() == 0) {
            totPen = null;
        } else {
            totPen = in.readInt();
        }
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

    public Integer getBussid() {
        return bussid;
    }

    public void setBussid(Integer bussid) {
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDOrM() {
        return dOrM;
    }

    public void setDOrM(String dOrM) {
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

    public Integer getNoOfCust() {
        return noOfCust;
    }

    public void setNoOfCust(Integer noOfCust) {
        this.noOfCust = noOfCust;
    }

    public Integer getTotCan() {
        return totCan;
    }

    public void setTotCan(Integer totCan) {
        this.totCan = totCan;
    }

    public Integer getTotEarn() {
        return totEarn;
    }

    public void setTotEarn(Integer totEarn) {
        this.totEarn = totEarn;
    }

    public Integer getTotPen() {
        return totPen;
    }

    public void setTotPen(Integer totPen) {
        this.totPen = totPen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(dOrM);
        dest.writeInt(price);
        dest.writeInt(totEarn);
        dest.writeString(imgurl);
        dest.writeInt(noOfCust);
        dest.writeString(payment);
        dest.writeString(address);
        dest.writeInt(bussid);
        dest.writeInt(totPen);
        dest.writeInt(totCan);
    }
}
