package com.daytoday.business.dailydelivery.MainHomeScreen.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.prolificinteractive.materialcalendarview.CalendarDay;

public class Dates {

    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Quantity")
    @Expose
    private Integer quantity;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
