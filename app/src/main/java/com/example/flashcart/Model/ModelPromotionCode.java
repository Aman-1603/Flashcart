package com.example.flashcart.Model;

public class ModelPromotionCode {

    String id,timestamp,PromoCode,PromoDescription,PromoPrice,MinOrderPrice,ExpireDate;

    public ModelPromotionCode(){

    }


    public ModelPromotionCode(String id, String timestamp, String promoCode, String promoDescription, String promoPrice, String minOrderPrice, String expireDate) {
        this.id = id;
        this.timestamp = timestamp;
        PromoCode = promoCode;
        PromoDescription = promoDescription;
        PromoPrice = promoPrice;
        MinOrderPrice = minOrderPrice;
        ExpireDate = expireDate;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPromoCode() {
        return PromoCode;
    }

    public void setPromoCode(String promoCode) {
        PromoCode = promoCode;
    }

    public String getPromoDescription() {
        return PromoDescription;
    }

    public void setPromoDescription(String promoDescription) {
        PromoDescription = promoDescription;
    }

    public String getPromoPrice() {
        return PromoPrice;
    }

    public void setPromoPrice(String promoPrice) {
        PromoPrice = promoPrice;
    }

    public String getMinOrderPrice() {
        return MinOrderPrice;
    }

    public void setMinOrderPrice(String minOrderPrice) {
        MinOrderPrice = minOrderPrice;
    }

    public String getExpireDate() {
        return ExpireDate;
    }

    public void setExpireDate(String expireDate) {
        ExpireDate = expireDate;
    }
}
