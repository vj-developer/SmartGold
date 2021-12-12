package com.greymatter.smartgold.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderResponse {

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

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("product_id")
        @Expose
        private String productId;
        @SerializedName("quantity")
        @Expose
        private String quantity;
        @SerializedName("buy_method")
        @Expose
        private String buyMethod;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("delivery_charges")
        @Expose
        private String deliveryCharges;
        @SerializedName("seller_id")
        @Expose
        private String sellerId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("discounted_price")
        @Expose
        private String discountedPrice;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("date_added")
        @Expose
        private String dateAdded;
        @SerializedName("is_approved")
        @Expose
        private String isApproved;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getBuyMethod() {
            return buyMethod;
        }

        public void setBuyMethod(String buyMethod) {
            this.buyMethod = buyMethod;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDeliveryCharges() {
            return deliveryCharges;
        }

        public void setDeliveryCharges(String deliveryCharges) {
            this.deliveryCharges = deliveryCharges;
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDiscountedPrice() {
            return discountedPrice;
        }

        public void setDiscountedPrice(String discountedPrice) {
            this.discountedPrice = discountedPrice;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDateAdded() {
            return dateAdded;
        }

        public void setDateAdded(String dateAdded) {
            this.dateAdded = dateAdded;
        }

        public String getIsApproved() {
            return isApproved;
        }

        public void setIsApproved(String isApproved) {
            this.isApproved = isApproved;
        }

    }
}
