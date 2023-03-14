package com.example.flashcart.Model;

import java.io.Serializable;

public class ModelProduct implements Serializable {
    private String ProductId, ProductTitle,ProductDescription,ProductCategory,ProductQuantity,
            ProductIcon,ProductPrice,ProductDiscountPrice,ProductDiscountNote,ProductDiscountAvailable,TimeStamp,uid;

    public ModelProduct() {
    }

    public ModelProduct(String productId, String productTitle, String productDescription, String productCategory, String productQuantity, String productIcon, String productPrice, String productDiscountPrice, String productDiscountNote, String productDiscountAvailable, String timeStamp, String uid) {
        ProductId = productId;
        ProductTitle = productTitle;
        ProductDescription = productDescription;
        ProductCategory = productCategory;
        ProductQuantity = productQuantity;
        ProductIcon = productIcon;
        ProductPrice = productPrice;
        ProductDiscountPrice = productDiscountPrice;
        ProductDiscountNote = productDiscountNote;
        ProductDiscountAvailable = productDiscountAvailable;
        TimeStamp = timeStamp;
        this.uid = uid;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductTitle() {
        return ProductTitle;
    }

    public void setProductTitle(String productTitle) {
        ProductTitle = productTitle;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

    public String getProductCategory() {
        return ProductCategory;
    }

    public void setProductCategory(String productCategory) {
        ProductCategory = productCategory;
    }

    public String getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        ProductQuantity = productQuantity;
    }

    public String getProductIcon() {
        return ProductIcon;
    }

    public void setProductIcon(String productIcon) {
        ProductIcon = productIcon;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getProductDiscountPrice() {
        return ProductDiscountPrice;
    }

    public void setProductDiscountPrice(String productDiscountPrice) {
        ProductDiscountPrice = productDiscountPrice;
    }

    public String getProductDiscountNote() {
        return ProductDiscountNote;
    }

    public void setProductDiscountNote(String productDiscountNote) {
        ProductDiscountNote = productDiscountNote;
    }

    public String getProductDiscountAvailable() {
        return ProductDiscountAvailable;
    }

    public void setProductDiscountAvailable(String productDiscountAvailable) {
        ProductDiscountAvailable = productDiscountAvailable;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
