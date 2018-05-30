package com.example.geomhelper;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserService {

    @FormUrlEncoded
    @POST("/signUp")
    Call<String> createUser(@Field("email") String email,
                            @Field("password") String password,
                            @Field("name") String name);

    @FormUrlEncoded
    @POST("/login")
    Call<String> login(@Field("email") String email,
                       @Field("password") String password);

    @FormUrlEncoded
    @POST("/loginWithSocial")
    Call<String> loginWithSocial(@Field("email") String email,
                       @Field("password") String password,
                                 @Field("name")String name);

    @FormUrlEncoded
    @PUT("/updateUser")
    Call<String> updateUser(@Field("id") String id,
                            @Field("param") String param,
                            @Field("value") String value);

    @GET("/getLeaders")
    Call<String> getLeaders();

    @FormUrlEncoded
    @PUT("/changeEmail")
    Call<String> changeEmail(@Field("id") String id,
                            @Field("password") String password,
                            @Field("email") String email);

    @FormUrlEncoded
    @PUT("/changePassword")
    Call<String> changePassword(@Field("id") String id,
                             @Field("password") String password,
                             @Field("newPassword") String newPassword);

}
