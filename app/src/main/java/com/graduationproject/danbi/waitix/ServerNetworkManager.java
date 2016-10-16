package com.graduationproject.danbi.waitix;

import retrofit2.Retrofit;

/**
 * Created by danbi_000 on 2016-10-16.
 */


public class ServerNetworkManager {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://127.0.0.1/")
            .build();
    APIService service = retrofit.create(APIService.class);
}
