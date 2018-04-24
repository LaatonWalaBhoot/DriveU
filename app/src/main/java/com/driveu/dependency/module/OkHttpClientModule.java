package com.driveu.dependency.module;

import com.driveu.dependency.scope.DriveUApplicationScope;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Aishwarya on 4/23/2018.
 */

@Module (includes = HttpLoggingInterceptorModule.class)
public class OkHttpClientModule {

    @Provides
    @DriveUApplicationScope
    public OkHttpClient okHttpClientLogger(HttpLoggingInterceptor loggingInterceptor) {
        return  new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request.Builder requestBuilder = originalRequest.newBuilder();
                        return chain.proceed(requestBuilder.build());
                    }
                })
                .addInterceptor(loggingInterceptor).build();
    }

}
