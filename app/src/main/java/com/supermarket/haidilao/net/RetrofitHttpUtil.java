package com.supermarket.haidilao.net;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

public class RetrofitHttpUtil {

    private static RetrofitHttpUtil netService = new RetrofitHttpUtil();

    public static RetrofitHttpUtil getInstance() {
        return netService;
    }

    private RetrofitHttpUtil() {
    }

    private ApiService service;

    public ApiService getService() {

        if (service == null) {

            //OkHttp配置
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//log日志
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .addInterceptor(interceptor)
                    .retryOnConnectionFailure(true)//错误重连
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
//                    .addNetworkInterceptor()//网络拦截器
                    .build();

            //Retrofit配置
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiService.WEB_ROOT)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .build();

            service = retrofit.create(ApiService.class);

        }

        return service;
    }

//    public ApiService getAddressService() {
//
////        if (service == null) {
//
//            //OkHttp配置
//            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//log日志
//            OkHttpClient client = new OkHttpClient().newBuilder()
//                    .addInterceptor(interceptor)
//                    .retryOnConnectionFailure(true)//错误重连
//                    .connectTimeout(10, TimeUnit.SECONDS)
//                    .readTimeout(10, TimeUnit.SECONDS)
////                    .addNetworkInterceptor()//网络拦截器
//                    .build();
//
//            //Retrofit配置
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(ApiService.WEB_ROOT_ADDRESS)
//                    .client(client)
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .addConverterFactory(FastJsonConverterFactory.create())
//                    .build();
//
//            service = retrofit.create(ApiService.class);
//
////        }
//
//        return service;
//    }


//    /**
//     * 根据城市名称获取经纬度
//     *
//     * @param
//     */
//    public void recodeAddress(Observer subscriber, String address, String output, String key, String city) {
//        getAddressService().recodeAddress(address, output, key, city)
//                .compose(schedulersTransformer())
//                .subscribe(subscriber);
//    }


    /**
     * 转换器
     *
     * @return
     */
    private ObservableTransformer schedulersTransformer() {

        return new ObservableTransformer() {

            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };

    }


    /**
     * 0是启动页 1是人事电话
     *
     * @param type
     */
    public void getInfo(Observer subscriber, String type) {
        getService().getInfo(type)
                .compose(schedulersTransformer())
                .subscribe(subscriber);
    }


    /**
     * 登录
     *
     * @param phone 手机号
     */
    public void login(Observer subscriber, String phone) {
        getService().login(phone)
                .compose(schedulersTransformer())
                .subscribe(subscriber);
    }


    /**
     * 商家列表
     *
     * @param key       搜索名称
     * @param latitude  维度
     * @param longitude 经度
     * @param pageIndex 当前页码
     * @param pageSize  显示数量
     */
    public void shopList(Observer subscriber, String key, String latitude, String longitude
            , String pageIndex, String pageSize) {
        getService().shopList(key, latitude, longitude, pageIndex, pageSize)
                .compose(schedulersTransformer())
                .subscribe(subscriber);
    }



    /**
     * 商家列表
     *
     * @param latitude  维度
     * @param longitude 经度
     * @param pageIndex 当前页码
     * @param pageSize  显示数量
     */
    public void nearbyList(Observer subscriber, String latitude, String longitude
            , String pageIndex, String pageSize) {
        getService().shopNearbyList(latitude, longitude, pageIndex, pageSize)
                .compose(schedulersTransformer())
                .subscribe(subscriber);
    }





    /**
     * 商家详情
     * @param cid 	商家ID
     * @param latitude 维度
     * @param longitude 经度
     */
    public void shopDetail(Observer subscriber, String cid, String latitude, String longitude) {
        getService().shopDetail(cid, latitude, longitude)
                .compose(schedulersTransformer())
                .subscribe(subscriber);
    }


    /**
     * 用户详情
     * @param uid 	用户ID
     */
    public void userInfo(Observer subscriber, Long uid) {
        getService().userInfo(uid)
                .compose(schedulersTransformer())
                .subscribe(subscriber);
    }


}
