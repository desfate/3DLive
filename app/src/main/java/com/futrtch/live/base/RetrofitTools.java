package com.futrtch.live.base;

import android.util.Log;

import com.futrtch.live.tencent.TCGlobalConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitTools {
    private final static String TAG = "RetrofitTools";

    private static OkHttpClient okHttpClient;

    private static Retrofit retrofit;

    public static <T> T getInstance(final Class<T> service) {
        if (okHttpClient == null) {
            synchronized (RetrofitTools.class) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpInteraptorLog());
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(5, TimeUnit.SECONDS)
                        .writeTimeout(5, TimeUnit.SECONDS)
                        .build();
            }
        }

        if (retrofit == null) {
            synchronized (RetrofitTools.class) {
                if(retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(TCGlobalConfig.APP_SVR_URL)         //BaseUrl
                            .client(okHttpClient)                       //请求的网络框架
                            .addConverterFactory(GsonConverterFactory.create())     //解析数据格式
                            .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // 使用RxJava作为回调适配器
                            .build();
                }
            }
        }
        return retrofit.create(service);
    }



    private static class HttpInteraptorLog implements HttpLoggingInterceptor.Logger{
        @Override
        public void log(String message) {
            Log.i("HttpRequest", message);
        }
    }
}
