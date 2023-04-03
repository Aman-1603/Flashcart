package com.example.flashcart.Model;

public class ModelOrderShop {

    String orderId,orderTime,orderStatus,orderFinalSubTotal,orderBy,orderTo,deliveyFee;

    public ModelOrderShop () {

    }

    public ModelOrderShop(String orderId, String orderTime, String orderStatus, String orderFinalSubTotal, String orderBy, String orderTo, String deliveyFee) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
        this.orderFinalSubTotal = orderFinalSubTotal;
        this.orderBy = orderBy;
        this.orderTo = orderTo;
        this.deliveyFee = deliveyFee;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderFinalSubTotal() {
        return orderFinalSubTotal;
    }

    public void setOrderFinalSubTotal(String orderFinalSubTotal) {
        this.orderFinalSubTotal = orderFinalSubTotal;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderTo() {
        return orderTo;
    }

    public void setOrderTo(String orderTo) {
        this.orderTo = orderTo;
    }

    public String getDeliveyFee() {
        return deliveyFee;
    }

    public void setDeliveyFee(String deliveyFee) {
        this.deliveyFee = deliveyFee;
    }
}
