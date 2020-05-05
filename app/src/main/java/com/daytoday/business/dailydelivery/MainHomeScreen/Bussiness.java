package com.daytoday.business.dailydelivery.MainHomeScreen;

public class Bussiness {

    private String productName, tarrif;
    private String price;
    private String earning;
    private String image;

    public Bussiness(String productName, String tarrif, String price, String earning, String image) {
        this.productName = productName;
        this.tarrif = tarrif;
        this.price = price;
        this.earning = earning;
        this.image = image;
    }

    public String getProductName() {
        return productName;
    }

    public String getTarrif() {
        return tarrif;
    }

    public String getPrice() {
        return price;
    }

    public String getEarning() {
        return earning;
    }

    public String getImage() {
        return image;
    }
}
