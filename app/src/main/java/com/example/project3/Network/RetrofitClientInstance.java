package com.example.project3.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    public static Retrofit getInstance(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
//                    .baseUrl("http://103.249.201.167:8000")
                    .baseUrl("http://192.168.0.103:8000")
//                    .baseUrl("http://192.168.1.35:8000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
