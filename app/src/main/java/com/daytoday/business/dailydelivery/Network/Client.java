package com.daytoday.business.dailydelivery.Network;

import android.util.Log;

import androidx.annotation.RestrictTo;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static Retrofit retrofit = null;
    private static Retrofit geoCodingRetrofit=null;

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.BASE_URL)
                    .client(getHeader())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getGeocodingClient(){
        if(geoCodingRetrofit==null){
            geoCodingRetrofit = new Retrofit.Builder()
                    .baseUrl("https://nominatim.openstreetmap.org/")
                    .client(getHeader())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return geoCodingRetrofit;
    }

    private static OkHttpClient getHeader() {
        return new OkHttpClient
                .Builder()
                .addInterceptor(getInterceptor())
                .build();
    }

    private static Interceptor getInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                FirebaseCrashlytics.getInstance().log(message);
                Log.i("message",message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
    }
}
