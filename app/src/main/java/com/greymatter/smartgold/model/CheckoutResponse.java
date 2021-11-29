package com.greymatter.smartgold.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckoutResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        @SerializedName("no_of_products")
        @Expose
        private Integer noOfProducts;
        @SerializedName("totalprice")
        @Expose
        private Integer totalprice;
        @SerializedName("orderprice")
        @Expose
        private Integer orderprice;
        @SerializedName("delivery_price")
        @Expose
        private Integer deliveryPrice;
        @SerializedName("grandtotal")
        @Expose
        private Integer grandtotal;
        @SerializedName("delivery_days")
        @Expose
        private Integer deliveryDays;
        @SerializedName("saved")
        @Expose
        private Integer saved;

        public Integer getNoOfProducts() {
            return noOfProducts;
        }

        public void setNoOfProducts(Integer noOfProducts) {
            this.noOfProducts = noOfProducts;
        }

        public Integer getTotalprice() {
            return totalprice;
        }

        public void setTotalprice(Integer totalprice) {
            this.totalprice = totalprice;
        }

        public Integer getOrderprice() {
            return orderprice;
        }

        public void setOrderprice(Integer orderprice) {
            this.orderprice = orderprice;
        }

        public Integer getDeliveryPrice() {
            return deliveryPrice;
        }

        public void setDeliveryPrice(Integer deliveryPrice) {
            this.deliveryPrice = deliveryPrice;
        }

        public Integer getDeliveryDays() {
            return deliveryDays;
        }

        public void setDeliveryDays(Integer deliveryDays) {
            this.deliveryDays = deliveryDays;
        }

        public Integer getSaved() {
            return saved;
        }

        public void setSaved(Integer saved) {
            this.saved = saved;
        }

        public Integer getGrandtotal() {
            return grandtotal;
        }

        public void setGrandtotal(Integer grandtotal) {
            this.grandtotal = grandtotal;
        }
    }
}
