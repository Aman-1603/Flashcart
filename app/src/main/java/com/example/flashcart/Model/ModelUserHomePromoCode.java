package com.example.flashcart.Model;

import android.widget.ImageView;

public class ModelUserHomePromoCode {


    String PromoDescription,PromoCode;

    public ModelUserHomePromoCode(){

    }


    public ModelUserHomePromoCode(String promoDescription, String promoCode) {
        PromoDescription = promoDescription;
        PromoCode = promoCode;
    }


    public String getPromoDescription() {
        return PromoDescription;
    }

    public void setPromoDescription(String promoDescription) {
        PromoDescription = promoDescription;
    }

    public String getPromoCode() {
        return PromoCode;
    }

    public void setPromoCode(String promoCode) {
        PromoCode = promoCode;
    }
}
