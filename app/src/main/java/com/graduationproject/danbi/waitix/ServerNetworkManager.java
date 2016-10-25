package com.graduationproject.danbi.waitix;

import retrofit2.Retrofit;

/**
 * Created by danbi_000 on 2016-10-16.
 */

//private static final String SERVER_URL = "https://api.server.net/"; //2부터 url뒤에 /를 입력해야 합니다.
//private static OkHttpClient client;

public class ServerNetworkManager {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://127.0.0.1/")
            .build();

/*
            .client(client)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) //Rxandroid를 사용하기 위해 추가(옵션)
            .addConverterFactory(GsonConverterFactory.create()) //Json Parser 추가
            .build().create(Retrofit.class); //인터페이스 연결
*/

    APIService service = retrofit.create(APIService.class);
}
