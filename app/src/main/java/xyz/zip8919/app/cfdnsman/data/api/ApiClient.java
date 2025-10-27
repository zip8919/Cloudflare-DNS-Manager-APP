package xyz.zip8919.app.cfdnsman.data.api;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://api.cloudflare.com/client/v4/";
    private static final int TIMEOUT = 30;
    
    private static Retrofit retrofit = null;
    
    public static CloudflareApiService getApiService(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(createOkHttpClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        }
        return retrofit.create(CloudflareApiService.class);
    }
    
    private static OkHttpClient createOkHttpClient(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS);
        
        // 添加授权拦截器
        builder.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                .header("Content-Type", "application/json")
                .method(original.method(), original.body());
            
            return chain.proceed(requestBuilder.build());
        });
        
        return builder.build();
    }
}
