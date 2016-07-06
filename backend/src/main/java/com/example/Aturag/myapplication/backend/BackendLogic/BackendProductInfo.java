package com.example.Aturag.myapplication.backend.BackendLogic;

/**
 * Created by Aturag on 23-Jun-16.
 */

public class BackendProductInfo {
    public String Name;
    public String Price;
    public int productIcon;
    public int ecommerceIcon;
    public String Url;

    public BackendProductInfo() {

    }

    public BackendProductInfo(String Name, String Price, int productIcon, int ecommerceIcon, String url) {
        this.Name = Name;
        this.Price = Price;
        this.productIcon = productIcon;
        this.ecommerceIcon = ecommerceIcon;
        this.Url = url;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public int getProductIcon() {
        return productIcon;
    }

    public void setProductIcon(int productIcon) {
        this.productIcon = productIcon;
    }

    public int getEcommerceIcon() {
        return ecommerceIcon;
    }

    public void setEcommerceIcon(int ecommerceIcon) {
        this.ecommerceIcon = ecommerceIcon;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
