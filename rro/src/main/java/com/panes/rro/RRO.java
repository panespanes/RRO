package com.panes.rro;

import android.text.TextUtils;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by panes on 2016/8/4.
 * Retrofit+RxJava+OkHttp3+fastjson
 * 获取一个配置了converter为FastJson的retrofit对象, 并做了一些简单封装.
 */
public class RRO {
    private static Retrofit retrofit;

    public static String getApiUrl() {
        return API_URL;
    }

    public static void setApiUrl(String apiUrl) {
        API_URL = apiUrl;
    }

    private static String API_URL;


    /**
     * 首次使用需配置baseUrl, 之后建议使用不带baseUrl参数的重载方法getApiService(Class<T> apiService)
     * @param apiService Restful标准的接口, 用于retrofit.create();
     * @param baseUrl Retrofit建议以"/"结尾
     * @param <T> JavaBean, 由fastJson解析
     * @return 网络请求的用法相当于调用本地接口apiService的自定义方法, 使用起来非常简单
     */
    public static <T> T getApiService(Class<T> apiService, String baseUrl) {
        if (!TextUtils.isEmpty(baseUrl)) {
            API_URL = baseUrl;
            mGetRetrofit();
        }
        return retrofit.create(apiService);
    }
    /**
     * 非首次使用不需配置baseUrl, 首次使用需要调用带baseUrl参数的重载方法getApiService(Class<T> apiService, String baseUrl)
     * @param apiService Restful标准的接口, 用于retrofit.create();
     * @param <T> JavaBean, 由fastJson解析
     * @return 网络请求的用法相当于调用本地接口apiService的自定义方法, 使用起来非常简单
     */
    public static <T> T getApiService(Class<T> apiService) {
        if (retrofit == null){
            mGetRetrofit();
        }
        return retrofit.create(apiService);
    }

    /**
     * 获取FastJson转换器, 用于retrofit.addConverterFactory()配置
     * @return
     */
    public Converter.Factory getFastJsonFactory(){
        return new FastJsonConvertFactory();
    }

    private static Retrofit mGetRetrofit(){
        validate();
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(new FastJsonConvertFactory())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }
    private static void validate() {
        if (TextUtils.isEmpty(API_URL)){
            throw new NullPointerException(){
                @Override
                public String getMessage() {
                    return "BASE_URL CANNOT BE NULL. INVOKE METHOD: getRetrofit(Class<T> clazz, String baseUrl) FIRST.";
                }
            };
        }
    }


}