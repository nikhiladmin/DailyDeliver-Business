package com.daytoday.business.dailydelivery.MainHomeScreen;

import com.prolificinteractive.materialcalendarview.CalendarDay;

public class Dates {
    CalendarDay day;
    String Quantity;

    public Dates(CalendarDay day, String quantity) {
        this.day = day;
        Quantity = quantity;
    }

    public CalendarDay getDay() {
        return day;
    }

    public void setDay(CalendarDay day) {
        this.day = day;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}
