package com.driveu.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.driveu.model.Location;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Aishwarya on 4/19/2018.
 */

public class DriveUApi  {

    public static final String BASE_API_URL = "http://10.0.2.2:8080";
    public final static int CONNECTION_TIMEOUT = 30;
    private final static int READ_TIMEOUT = 30;
    private final static int WRITE_TIMEOUT = 30;

    /************************************
     * PRIVATE STATIC FIELDS
     ************************************/
    private static volatile DriveUApi instance;

    /************************************
     * PRIVATE FIELDS
     ************************************/
    private DriveUApiInterface service;

    /************************************
     * PRIVATE METHODS
     ************************************/
    private DriveUApi() {}

    /************************************
     * PUBLIC STATIC METHODS
     ************************************/
    public static DriveUApi getInstance() {
        if(instance==null) {
            synchronized (DriveUApi.class) {
                if(instance==null) {
                    instance = new DriveUApi();
                    instance.init();
                }
            }
        }
        return instance;
    }

    public void getNextLocation(@NonNull final NextLocationListener nextLocationListener) {
        service.getNextLocation().enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, retrofit2.Response<Location> response) {
                if(response.errorBody()!=null) {
                    try {
                        nextLocationListener.onNextLocationFailure(new NextLocationException(response.errorBody().string()));
                    } catch (IOException e) {
                        nextLocationListener.onNextLocationFailure(new NextLocationException("IOException when parsing error status"));
                        Log.d("MESSAGE: ","onResponse - IOException - " + Log.getStackTraceString(e));
                        e.printStackTrace();
                    }
                    return;
                }
                nextLocationListener.onNextLocationSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                nextLocationListener.onNextLocationFailure(new NextLocationException(t.getMessage()));
            }
        });
    }

    /************************************
     * PUBLIC INTERFACES
     ************************************/

    public interface NextLocationListener {

        void onNextLocationSuccess(Location location);

        void onNextLocationFailure(Throwable t);
    }

    /************************************
     * EXCEPTIONS
     ************************************/
    public static class NextLocationException extends Exception {
        private NextLocationException(String response) {
            super(response);
        }
    }

    /************************************
     * PUBLIC METHODS
     ************************************/
    private void init() {

        Gson gson = new GsonBuilder().create();
        OkHttpClient httpClient = createHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);

        service = retrofit.create(DriveUApiInterface.class);
    }

    private OkHttpClient createHttpClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request.Builder requestBuilder = originalRequest.newBuilder();
                        return chain.proceed(requestBuilder.build());
                    }
                })
                .addInterceptor(logging);

        return builder.build();
    }
}
