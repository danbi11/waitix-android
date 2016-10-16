package com.graduationproject.danbi.waitix;

import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by danbi_000 on 2016-10-16.
 */

interface APIService {
    @POST("/user/cancel")
    String cancelRequest(@Field("unum") String unum);

    @POST("/user/request")
    String request(@Field("unum") String unum, @Field("snum") int snum, @Field("pon") int pon);

    @GET("/store/list")
    String getStoreList();

    @GET("/user/verify")
    String verify(@Field("unum") String unum);
}

