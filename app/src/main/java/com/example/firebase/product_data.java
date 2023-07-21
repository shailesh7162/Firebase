package com.example.firebase;

public class product_data
{
    String id;
    String name;
    String price;
    String dec;
    String imgUrl;

    public product_data(String id, String name, String price, String dec, String imgUrl)
    {
        this.id=id;
        this.name=name;
        this.price=price;
        this.dec=dec;
        this.imgUrl=imgUrl;
    }

    public product_data() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

