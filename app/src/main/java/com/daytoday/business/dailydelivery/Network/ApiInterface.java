package com.daytoday.business.dailydelivery.Network;

import com.daytoday.business.dailydelivery.Network.Response.AuthUserCheckResponse;
import com.daytoday.business.dailydelivery.Network.Response.AuthUserResponse;
import com.daytoday.business.dailydelivery.Network.Response.BussDetailsResponse;
import com.daytoday.business.dailydelivery.Network.Response.BussRelCustResponse;
import com.daytoday.business.dailydelivery.Network.Response.DayWiseResponse;
import com.daytoday.business.dailydelivery.Network.Response.GeocodingResponse;
import com.daytoday.business.dailydelivery.Network.Response.OTPSendResponse;
import com.daytoday.business.dailydelivery.Network.Response.OTPVerifyResponse;
import com.daytoday.business.dailydelivery.Network.Response.YesNoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    String BASE_URL = "https://dailydeliver.000webhostapp.com/v1/";

    @GET("insert-buss-user-details")
    Call<YesNoResponse> addBussUserDetails(@Query("userid") String userId, @Query("username") String userName
            , @Query("userphone") String userPhone, @Query("useradd") String userAdd,@Query("useremail") String email,@Query("provider") int provider);

    @GET("insert-cust-user-details")
    Call<YesNoResponse> addCustUserDetails(@Query("userid") String userId,@Query("username") String userName
            ,@Query("userphone") String userPhone,@Query("useradd") String userAdd);

    @GET("login-buss")
    Call<AuthUserResponse> loginUser(@Query("bussid") String bussid);

    @GET("insert-buss-details")
    Call<YesNoResponse> addBussDetails(@Query("bussname") String bussName,@Query("monordaily") String monOrDaily
            ,@Query("paymode") String payMode,@Query("price") String price,@Query("userId") String userId,@Query("imageurl") String imageurl,
                                       @Query("phone")String phone,@Query("address") String address);

    @GET("insert-buss-cust-details")
    Call<YesNoResponse> addBussCustDetails(@Query("bussid") String bussId,@Query("custid") String custId);

    @GET("insert-pending")
    Call<YesNoResponse> addPendingRequest(@Query("bussUserId") String bussUserId,@Query("quantity") String quantity);

    @GET("fetch-buss-list")
    Call<BussDetailsResponse> getBussList(@Query("bussid") String bussId);

    @GET("fetch-rel-cust")
    Call<BussRelCustResponse> getRelCust(@Query("bussid") String bussId);

    @GET("update-buss-details")
    Call<YesNoResponse> updateBussDetails(@Query("bussid") String bussid,@Query("name") String name,@Query("phone") String phone
            ,@Query("address") String address,@Query("price") String price,@Query("paymode") String payMode,@Query("imageurl")String imageurl);

    @GET("update-buss-user-details")
    Call<YesNoResponse> updateBussUserDetails(@Query("bussuserid") String bussuserid,@Query("username") String userName
            ,@Query("userphone") String userPhone,@Query("useraddress") String userAdd);

    @GET("update-cust-user-details")
    Call<YesNoResponse> updateCutUserDetails(@Query("custid") String custid,@Query("name") String name
            ,@Query("phone") String phone,@Query("address") String address);

   /* @GET("fetch-emp")
    Call<> getEmp(@Query("") String );*/

    @GET("fetch-daywise")
    Call<DayWiseResponse> getDayWise(@Query("busscustid") String bussCustId);


    @GET("reverse")
    Call<GeocodingResponse> getReverseGeocoding(@Query("lat") double lat,@Query("lon") double lon,@Query("zoom") int zoom,@Query("addressdetails") int addressdetails,@Query("format") String format);

    @GET("send-login-otp")
    Call<OTPSendResponse> getOTPSend(@Query("email") String email);

    @GET("verify-otp")
    Call<OTPVerifyResponse> getOTPVerify(@Query("otp") String otp,@Query("userid") String userid);

    @GET("is-registered-buss")
    Call<AuthUserCheckResponse> isRegisteredUser(@Query("email") String email);
}
