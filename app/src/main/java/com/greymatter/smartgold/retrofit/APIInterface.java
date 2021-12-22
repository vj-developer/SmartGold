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
    @POST("get-offer-lock-by-id.php")
    Call<LockedOfferResponse> offer_locked(@Field(Constants.USERID) String user_id);

    @FormUrlEncoded
    @POST("allproducts.php")
    Call<ProductListResponse> category_products(@Field(Constants.CATEGORY_ID) String category_id);

    @FormUrlEncoded
    @POST("filter_products.php")
    Call<ProductListResponse> filter_products(@Field(Constants.FROM_PRICE_RANGE) String from_price_range,
                                                @Field(Constants.TO_PRICE_RANGE) String to_price_range,
                                                @Field(Constants.ORDER_BY) String sort,
                                              @Field(Constants.CATEGORY_ID) String category_id);

    @FormUrlEncoded
    @POST("search_products.php")
    Call<ProductListResponse> search_products(@Field(Constants.SEARCH_TERM) String query);



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

    @FormUrlEncoded
    @POST("add_to_cart.php")
    Call<AddToCartResponse> add_to_cart(@Field(Constants.USERID) String user_id,
                                        @Field(Constants.PRODUCT_ID) String product_id,
                                        @Field(Constants.QUANTITY) String quantity);

    @FormUrlEncoded
    @POST("cart_list.php")
    Call<CartListResponse> cart_list(@Field(Constants.USERID) String user_id);

    @FormUrlEncoded
    @POST("remove_cart_item.php")
    Call<AddToCartResponse> remove_from_cart(@Field(Constants.USERID) String user_id,
                                            @Field(Constants.CARTID) String cart_id);

    @FormUrlEncoded
    @POST("make_address_default.php")
    Call<AddToCartResponse> make_address_default(@Field(Constants.USERID) String user_id,
                                            @Field(Constants.ADDRESS_ID) String address_id);

    @FormUrlEncoded
    @POST("check_out.php")
    Call<CheckoutResponse> check_out(@Field(Constants.USERID) String user_id,
                                     @Field(Constants.PAYMENT_METHOD) String payment_method );

    @FormUrlEncoded
    @POST("get_default_address.php")
    Call<DefaultAddressResponse> get_default_address(@Field(Constants.USERID) String user_id);

    @FormUrlEncoded
    @POST("place_order.php")
    Call<CheckoutResponse> place_order(@Field(Constants.USERID) String user_id,
                                       @Field(Constants.BUY_METHOD) String payment_method );

    @FormUrlEncoded
    @POST("orders_list.php")
    Call<OrderResponse> order_list(@Field(Constants.USERID) String user_id);
}