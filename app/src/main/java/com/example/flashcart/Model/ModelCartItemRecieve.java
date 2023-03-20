package com.example.flashcart.Model;

import java.io.Serializable;

public class ModelCartItemRecieve implements Serializable {

    String uid,quantity,ProductIcon,price,ShopUid,title,priceEach,itemUid;


    public ModelCartItemRecieve(){

    }


    public ModelCartItemRecieve(String uid, String quantity, String productIcon, String price, String shopUid, String title, String priceEach, String itemUid) {
        this.uid = uid;
        this.quantity = quantity;
        ProductIcon = productIcon;
        this.price = price;
        ShopUid = shopUid;
        this.title = title;
        this.priceEach = priceEach;
        this.itemUid = itemUid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductIcon() {
        return ProductIcon;
    }

    public void setProductIcon(String productIcon) {
        ProductIcon = productIcon;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShopUid() {
        return ShopUid;
    }

    public void setShopUid(String shopUid) {
        ShopUid = shopUid;
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

    public String getItemUid() {
        return itemUid;
    }

    public void setItemUid(String itemUid) {
        this.itemUid = itemUid;
    }
}
