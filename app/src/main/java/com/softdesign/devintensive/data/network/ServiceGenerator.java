package com.softdesign.devintensive.data.network;

import com.softdesign.devintensive.data.network.interceptors.HeaderInterceptor;
import com.softdesign.devintensive.utils.AppConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static OkHttpClient.Builder shttpclient=new OkHttpClient.Builder();

    private static Retrofit.Builder sBuilder=new Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

public static <S> S createService(Class<S> serviceClass){

    HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor();
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    shttpclient.addInterceptor(new HeaderInterceptor());
    shttpclient.addInterceptor(loggingInterceptor);

    Retrofit retrofit = sBuilder
            .client(shttpclient.build())
            .build();
    return retrofit.create(serviceClass);
}
}
