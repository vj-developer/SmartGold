package com.greymatter.smartgold.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OfferLockResponse {

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
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("store_name")
        @Expose
        private String storeName;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("store_url")
        @Expose
        private String storeUrl;
        @SerializedName("logo")
        @Expose
        private String logo;
        @SerializedName("store_description")
        @Expose
        private String storeDescription;
        @SerializedName("street")
        @Expose
        private String street;
        @SerializedName("pincode")
        @Expose
        private String pincode;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("account_number")
        @Expose
        private String accountNumber;
        @SerializedName("bank_ifsc_code")
        @Expose
        private String bankIfscCode;
        @SerializedName("account_name")
        @Expose
        private String accountName;
        @SerializedName("bank_name")
        @Expose
        private String bankName;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("last_updated")
        @Expose
        private String lastUpdated;
        @SerializedName("date_created")
        @Expose
        private String dateCreated;
        @SerializedName("national_identity_card")
        @Expose
        private String nationalIdentityCard;
        @SerializedName("address_proof")
        @Expose
        private String addressProof;
        @SerializedName("pan_number")
        @Expose
        private String panNumber;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("valid_till")
        @Expose
        private String validTill;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getStoreUrl() {
            return storeUrl;
        }

        public void setStoreUrl(String storeUrl) {
            this.storeUrl = storeUrl;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getStoreDescription() {
            return storeDescription;
        }

        public void setStoreDescription(String storeDescription) {
            this.storeDescription = storeDescription;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getBankIfscCode() {
            return bankIfscCode;
        }

        public void setBankIfscCode(String bankIfscCode) {
            this.bankIfscCode = bankIfscCode;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(String lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        public String getDateCreated() {
            return dateCreated;
        }

        public void setDateCreated(String dateCreated) {
            this.dateCreated = dateCreated;
        }

        public String getNationalIdentityCard() {
            return nationalIdentityCard;
        }

        public void setNationalIdentityCard(String nationalIdentityCard) {
            this.nationalIdentityCard = nationalIdentityCard;
        }

        public String getAddressProof() {
            return addressProof;
        }

        public void setAddressProof(String addressProof) {
            this.addressProof = addressProof;
        }

        public String getPanNumber() {
            return panNumber;
        }

        public void setPanNumber(String panNumber) {
            this.panNumber = panNumber;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getValidTill() {
            return validTill;
        }

        public void setValidTill(String validTill) {
            this.validTill = validTill;
        }
    }
}
