package com.driveu.dependency.module;

import com.driveu.dependency.scope.DriveUApplicationScope;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Aishwarya on 4/23/2018.
 */

@Module (includes = {GsonModule.class, OkHttpClientModule.class})
public class RetrofitModule {

    public static final String BASE_API_URL = "http://10.0.2.2:8080";
    public final static int CONNECTION_TIMEOUT = 30;
    private final static int READ_TIMEOUT = 30;
    private final static int WRITE_TIMEOUT = 30;

    @Provides
    @DriveUApplicationScope
    public Retrofit retrofit(OkHttpClient okHttpClient, Gson gson) {

        OkHttpClient.Builder httpClientBuilder = okHttpClient.newBuilder();
        httpClientBuilder
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);

        return new Retrofit.Builder()
                .client(okHttpClient)
                .client(httpClientBuilder.build())
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }


}
