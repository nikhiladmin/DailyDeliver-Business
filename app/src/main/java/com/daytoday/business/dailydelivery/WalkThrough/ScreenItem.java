package com.daytoday.business.dailydelivery.WalkThrough;

public class ScreenItem {
    int ScreenImg;
    String title;
    String description;

    public ScreenItem(int screenImg, String description,String title) {
        ScreenImg = screenImg;
        this.description = description;
        this.title = title;
    }

    public void setScreenImg(int screenImg) {
        ScreenImg = screenImg;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }

    public int getScreenImg() {
        return ScreenImg;
    }

    public String getTitle() {
        return title;
    }
}
