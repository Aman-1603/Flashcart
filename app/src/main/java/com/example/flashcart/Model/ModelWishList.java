package com.example.flashcart.Model;

public class ModelWishList {

    String title,priceEach,ProductIcon;


    public ModelWishList(){

    }

    public ModelWishList(String title, String priceEach, String productIcon) {
        this.title = title;
        this.priceEach = priceEach;
        ProductIcon = productIcon;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriceEach() {
        return priceEach;
    }

    public void setPriceEach(String priceEach) {
        this.priceEach = priceEach;
    }

    public String getProductIcon() {
        return ProductIcon;
    }

    public void setProductIcon(String productIcon) {
        ProductIcon = productIcon;
    }
}
