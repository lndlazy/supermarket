package com.supermarket.haidilao.net;


import com.supermarket.haidilao.bean.Basebean;
import com.supermarket.haidilao.bean.InfoEntity;
import com.supermarket.haidilao.bean.LoginEntity;
import com.supermarket.haidilao.bean.ShopDetailEntity;
import com.supermarket.haidilao.bean.ShopListEntity;
import com.supermarket.haidilao.bean.UserEntity;
import com.supermarket.haidilao.utils.LogUtil;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 请求的接口
 * Created by Aaron on 17/2/14.
 */
public interface ApiService {

    String WEB_ROOT = "http://points.xunxinsoft.com";

    //启动页
    String START_UP_TYPE = "0";

    String PM_TYPE = "1";
    String YS_TYPE = "5";//隐私政策

    // 0是启动页 1是人事电话
    @GET("/api/public/sysSet.json")
    Observable<InfoEntity> getInfo(@Query("type") String type);

    //登录
    @GET("/api/login/login.json")
    Observable<LoginEntity> login(@Query("phone") String phone);


    //商家列表
    @GET("/api/corp/corps.json")
    Observable<ShopListEntity> shopList(@Query("key") String key, @Query("latitude") String latitude
            , @Query("longitude") String longitude, @Query("pageIndex") String pageIndex, @Query("pageSize") String pageSize);

    //商家详情
    @FormUrlEncoded
    @POST("/api/corp/detail.json")
    Observable<ShopDetailEntity> shopDetail(@Field("cid") String cid, @Field("latitude") String latitude
            , @Field("longitude") String longitude);

    //商家附近列表
    @FormUrlEncoded
    @POST("/api/corp/nearby.json")
    Observable<ShopListEntity> shopNearbyList(@Field("latitude") String latitude, @Field("longitude") String longitude
            , @Field("pageIndex") String pageIndex, @Field("pageSize") String pageSize);

    //用户详情
//    @FormUrlEncoded
    @POST("/api/user/detail.json")
    Observable<UserEntity> userInfo(@Header("uid") Long uid);


}
