package com.greymatter.smartgold.retrofit;

import com.greymatter.smartgold.model.AddAddressResponse;
import com.greymatter.smartgold.model.AddressListResponse;
import com.greymatter.smartgold.model.BannerListResponse;
import com.greymatter.smartgold.model.BudgetRangeResponse;
import com.greymatter.smartgold.model.CategoryResponse;
import com.greymatter.smartgold.model.LoginResponse;
import com.greymatter.smartgold.model.OfferLockResponse;
import com.greymatter.smartgold.model.PriceDurationResponse;
import com.greymatter.smartgold.model.ProductListResponse;
import com.greymatter.smartgold.model.RegisterResponse;
import com.greymatter.smartgold.model.SmartOffersResponse;
import com.greymatter.smartgold.utils.Constants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIInterface {

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> login(@Field(Constants.MOBILE) String mobile);

    @FormUrlEncoded
    @POST("register.php")
    Call<RegisterResponse> register(@Field(Constants.MOBILE) String mobile, @Field(Constants.NAME) String name, @Field(Constants.EMAIL) String email);

    @FormUrlEncoded
    @POST("add-address.php")
    Call<AddAddressResponse> add_address(@Field(Constants.USERID) String user_id,
                                         @Field(Constants.NAME) String name,
                                         @Field(Constants.ADDRESS) String address,
                                         @Field(Constants.LANDMARK) String landmark,
                                         @Field(Constants.CITY) String city,
                                         @Field(Constants.AREA) String area,
                                         @Field(Constants.PINCODE) String pincode);

    @FormUrlEncoded
    @POST("address-list.php")
    Call<AddressListResponse> address_list(@Field(Constants.USERID) String user_id);


    @GET("banner-list.php")
    Call<BannerListResponse> banner_list();

    @GET("budget-range.php")
    Call<BudgetRangeResponse> budget_range();

    @GET("categorylist.php")
    Call<CategoryResponse> category();
    @GET("allproducts.php")
    Call<ProductListResponse> product();

    @FormUrlEncoded
    @POST("productlist-seller.php")
    Call<CategoryResponse> available_products(@Field(Constants.SELLER_ID) String shop_id);

    @FormUrlEncoded
    @POST("offer-lock.php")
    Call<OfferLockResponse> lock_offer(@Field(Constants.USERID) String user_id,
                                       @Field(Constants.SELLER_ID) String seller_id,
                                       @Field(Constants.OFFER_ID) String offer_id,
                                       @Field(Constants.PAID_AMT) String paid_amt
                                              );

    @GET("price-duration.php")
    Call<PriceDurationResponse> price_duration();

    @FormUrlEncoded
    @POST("smart-offers.php")
    Call<SmartOffersResponse> smart_offers(@Field(Constants.BUDGET_ID) String budget_range_id,
                                           @Field(Constants.LATITUDE) String latitude,
                                           @Field(Constants.LONGITUDE) String longitude) ;
}