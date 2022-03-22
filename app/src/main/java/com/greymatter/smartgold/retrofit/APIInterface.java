package com.greymatter.smartgold.retrofit;

import com.greymatter.smartgold.model.AddAddressResponse;
import com.greymatter.smartgold.model.AddToCartResponse;
import com.greymatter.smartgold.model.AddressListResponse;
import com.greymatter.smartgold.model.BannerListResponse;
import com.greymatter.smartgold.model.BudgetRangeResponse;
import com.greymatter.smartgold.model.CartListResponse;
import com.greymatter.smartgold.model.CategoryResponse;
import com.greymatter.smartgold.model.CheckoutResponse;
import com.greymatter.smartgold.model.DefaultAddressResponse;
import com.greymatter.smartgold.model.LockedOfferResponse;
import com.greymatter.smartgold.model.LoginResponse;
import com.greymatter.smartgold.model.OfferLockResponse;
import com.greymatter.smartgold.model.OrderResponse;
import com.greymatter.smartgold.model.PriceDurationResponse;
import com.greymatter.smartgold.model.ProductListResponse;
import com.greymatter.smartgold.model.RegisterResponse;
import com.greymatter.smartgold.model.SmartOffersResponse;
import com.greymatter.smartgold.model.StoreResponse;
import com.greymatter.smartgold.utils.Constants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIInterface {

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> login(@Header("Authorization") String auth,
                              @Field(Constants.AccessKey) String accesskey,
                              @Field(Constants.MOBILE) String mobile);

    @FormUrlEncoded
    @POST("register.php")
    Call<RegisterResponse> register(@Header("Authorization") String auth,
                                    @Field(Constants.AccessKey) String accesskey,
                                    @Field(Constants.MOBILE) String mobile, @Field(Constants.NAME) String name, @Field(Constants.EMAIL) String email);

    @FormUrlEncoded
    @POST("add-address.php")
    Call<AddAddressResponse> add_address(@Header("Authorization") String auth,
                                         @Field(Constants.AccessKey) String accesskey,
                                         @Field(Constants.USERID) String user_id,
                                         @Field(Constants.NAME) String name,
                                         @Field(Constants.ADDRESS) String address,
                                         @Field(Constants.LANDMARK) String landmark,
                                         @Field(Constants.CITY) String city,
                                         @Field(Constants.AREA) String area,
                                         @Field(Constants.PINCODE) String pincode);

    @FormUrlEncoded
    @POST("address-list.php")
    Call<AddressListResponse> address_list(@Header("Authorization") String auth,
                                           @Field(Constants.AccessKey) String accesskey,
                                           @Field(Constants.USERID) String user_id);

    @FormUrlEncoded
    @POST("banner-list.php")
    Call<BannerListResponse> banner_list(@Header("Authorization") String auth,
                                         @Field(Constants.AccessKey) String accesskey);
    @FormUrlEncoded
    @POST("budget-range.php")
    Call<BudgetRangeResponse> budget_range(@Header("Authorization") String auth,
                                           @Field(Constants.AccessKey) String accesskey);
    @FormUrlEncoded
    @POST("categorylist.php")
    Call<CategoryResponse> category(@Header("Authorization") String auth,
                                    @Field(Constants.AccessKey) String accesskey);

    @FormUrlEncoded
    @POST("allproducts.php")
    Call<ProductListResponse> product(@Header("Authorization") String auth,
                                      @Field(Constants.AccessKey) String accesskey);

    @FormUrlEncoded
    @POST("getsellers.php")
    Call<StoreResponse> getSellers(@Header("Authorization") String auth,
                                   @Field(Constants.AccessKey) String accesskey,
                                   @Field(Constants.LATITUDE) String latitude,
                                   @Field(Constants.LONGITUDE) String longitude,
                                   @Field(Constants.RANGE_TO) String range_to
                                      );

    @FormUrlEncoded
    @POST("get-offer-lock-by-id.php")
    Call<LockedOfferResponse> offer_locked(@Header("Authorization") String auth,
                                           @Field(Constants.AccessKey) String accesskey,
                                           @Field(Constants.USERID) String user_id);

    @FormUrlEncoded
    @POST("allproducts.php")
    Call<ProductListResponse> category_products(@Header("Authorization") String auth,
                                                @Field(Constants.AccessKey) String accesskey,
                                                @Field(Constants.CATEGORY_ID) String category_id);

    @FormUrlEncoded
    @POST("filter_products.php")
    Call<ProductListResponse> filter_products(@Header("Authorization") String auth,
                                              @Field(Constants.AccessKey) String accesskey,
                                              @Field(Constants.FROM_PRICE_RANGE) String from_price_range,
                                              @Field(Constants.TO_PRICE_RANGE) String to_price_range,
                                              @Field(Constants.ORDER_BY) String sort,
                                              @Field(Constants.CATEGORY_ID) String category_id);

    @FormUrlEncoded
    @POST("search_products.php")
    Call<ProductListResponse> search_products(@Header("Authorization") String auth,
                                              @Field(Constants.AccessKey) String accesskey,
                                              @Field(Constants.SEARCH_TERM) String query);



    @FormUrlEncoded
    @POST("categorylist-seller.php")
    Call<CategoryResponse> available_products(@Header("Authorization") String auth,
                                              @Field(Constants.AccessKey) String accesskey,
                                              @Field(Constants.SELLER_ID) String shop_id);

    @FormUrlEncoded
    @POST("offer-lock.php")
    Call<OfferLockResponse> lock_offer(@Header("Authorization") String auth,
                                       @Field(Constants.AccessKey) String accesskey,
                                       @Field(Constants.USERID) String user_id,
                                       @Field(Constants.SELLER_ID) String seller_id,
                                       @Field(Constants.OFFER_ID) String offer_id,
                                       @Field(Constants.PAID_AMT) String paid_amt);


    @FormUrlEncoded
    @POST("price-duration.php")
    Call<PriceDurationResponse> price_duration(@Header("Authorization") String auth,
                                               @Field(Constants.AccessKey) String accesskey);

    @FormUrlEncoded
    @POST("smart-offers.php")
    Call<SmartOffersResponse> smart_offers(@Header("Authorization") String auth,
                                           @Field(Constants.AccessKey) String accesskey,
                                            @Field(Constants.BUDGET_ID) String budget_range_id,
                                           @Field(Constants.LATITUDE) String latitude,
                                           @Field(Constants.LONGITUDE) String longitude,
                                           @Field(Constants.RANGE_TO) String range_to) ;

    @FormUrlEncoded
    @POST("add_to_cart.php")
    Call<AddToCartResponse> add_to_cart(@Header("Authorization") String auth,
                                        @Field(Constants.AccessKey) String accesskey,
                                        @Field(Constants.USERID) String user_id,
                                        @Field(Constants.PRODUCT_ID) String product_id,
                                        @Field(Constants.QUANTITY) String quantity);

    @FormUrlEncoded
    @POST("cart_list.php")
    Call<CartListResponse> cart_list(@Header("Authorization") String auth,
                                     @Field(Constants.AccessKey) String accesskey,
                                     @Field(Constants.USERID) String user_id);

    @FormUrlEncoded
    @POST("remove_cart_item.php")
    Call<AddToCartResponse> remove_from_cart(@Header("Authorization") String auth,
                                             @Field(Constants.AccessKey) String accesskey,
                                             @Field(Constants.USERID) String user_id,
                                            @Field(Constants.CARTID) String cart_id);

    @FormUrlEncoded
    @POST("make_address_default.php")
    Call<AddToCartResponse> make_address_default(@Header("Authorization") String auth,
                                                 @Field(Constants.AccessKey) String accesskey,
                                                 @Field(Constants.USERID) String user_id,
                                            @Field(Constants.ADDRESS_ID) String address_id);

    @FormUrlEncoded
    @POST("check_out.php")
    Call<CheckoutResponse> check_out(@Header("Authorization") String auth,
                                     @Field(Constants.AccessKey) String accesskey,
                                     @Field(Constants.USERID) String user_id,
                                     @Field(Constants.PAYMENT_METHOD) String payment_method );

    @FormUrlEncoded
    @POST("get_default_address.php")
    Call<DefaultAddressResponse> get_default_address(@Header("Authorization") String auth,
                                                     @Field(Constants.AccessKey) String accesskey,
                                                     @Field(Constants.USERID) String user_id);

    @FormUrlEncoded
    @POST("place_order.php")
    Call<CheckoutResponse> place_order(@Header("Authorization") String auth,
                                       @Field(Constants.AccessKey) String accesskey,
                                       @Field(Constants.USERID) String user_id,
                                       @Field(Constants.BUY_METHOD) String payment_method,
                                       @Field(Constants.IS_PAID) boolean is_paid );

    @FormUrlEncoded
    @POST("orders_list.php")
    Call<OrderResponse> order_list(@Header("Authorization") String auth,
                                   @Field(Constants.AccessKey) String accesskey,
                                   @Field(Constants.USERID) String user_id);
}