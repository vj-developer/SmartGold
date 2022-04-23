package com.greymatter.smartgold.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SmartOffersResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("nick_name")
        @Expose
        private String nickName;
        @SerializedName("budget")
        @Expose
        private String budget;
        @SerializedName("gram_price")
        @Expose
        private String gramPrice;
        @SerializedName("wastage")
        @Expose
        private String wastage;
        @SerializedName("total_locked")
        @Expose
        private int total_locked;
        @SerializedName("total_products")
        @Expose
        private int total_products;
        @SerializedName("max_locked")
        @Expose
        private String maxLocked;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("valid_date")
        @Expose
        private String validDate;
        @SerializedName("distance")
        @Expose
        private Double distance;
        @SerializedName("seller_id")
        @Expose
        private String seller_id;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("offer_details")
        @Expose
        private String offerDetails;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getBudget() {
            return budget;
        }

        public void setBudget(String budget) {
            this.budget = budget;
        }

        public String getGramPrice() {
            return gramPrice;
        }

        public void setGramPrice(String gramPrice) {
            this.gramPrice = gramPrice;
        }

        public String getWastage() {
            return wastage;
        }

        public void setWastage(String wastage) {
            this.wastage = wastage;
        }

        public String getMaxLocked() {
            return maxLocked;
        }

        public void setMaxLocked(String maxLocked) {
            this.maxLocked = maxLocked;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getValidDate() {
            return validDate;
        }

        public void setValidDate(String validDate) {
            this.validDate = validDate;
        }

        public Double getDistance() {
            return distance;
        }

        public void setDistance(Double distance) {
            this.distance = distance;
        }

        public String getSeller_id() {
            return seller_id;
        }

        public void setSeller_id(String seller_id) {
            this.seller_id = seller_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOfferDetails() {
            return offerDetails;
        }

        public void setOfferDetails(String offerDetails) {
            this.offerDetails = offerDetails;
        }

        public int getTotal_locked() {
            return total_locked;
        }

        public void setTotal_locked(int total_locked) {
            this.total_locked = total_locked;
        }

        public int getTotal_products() {
            return total_products;
        }

        public void setTotal_products(int total_products) {
            this.total_products = total_products;
        }
    }
}
