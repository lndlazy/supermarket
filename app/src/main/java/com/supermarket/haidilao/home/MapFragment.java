package com.supermarket.haidilao.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.supermarket.haidilao.R;
import com.supermarket.haidilao.base.BaseFragment;
import com.supermarket.haidilao.bean.ShopListEntity;
import com.supermarket.haidilao.detail.ShopDetailActivity;
import com.supermarket.haidilao.sheet.ActionSheetDialog;
import com.supermarket.haidilao.utils.CommUtils;
import com.supermarket.haidilao.utils.Constant;
import com.supermarket.haidilao.utils.LogUtil;
import com.supermarket.haidilao.utils.OpenLocalMapUtil;

import java.util.List;

public class MapFragment extends BaseFragment implements AMap.InfoWindowAdapter, View.OnClickListener {

    private static final String TAG = "MapFragment";

    private MapView mapView;
    private AMapLocationClient mLocationClient;
    private AMap aMap;

    private Marker mLocMarker;
    private Marker chooseLocMarker;
    private AMapLocation mapLocation;
    private AMapLocationListener mLocationListener;


    private static final int MARKER_ID = 90;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(getContext(), R.layout.fragment_map, null);
//        findViewById(view);
        initMap(savedInstanceState, view);

        return view;
    }


    private void initMap(Bundle savedInstanceState, View view) {
        ImageView ivMyLocation = view.findViewById(R.id.ivMyLocation);
        ivMyLocation.setOnClickListener(this);
        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        mLocationClient = new AMapLocationClient(getActivity());
        aMap = mapView.getMap();
        aMap.getUiSettings().setZoomControlsEnabled(false);
        setMap();
    }


    private boolean firstLocation = true;

    public void setMap() {

        if (aMap == null || mLocationClient == null)
            return;

        //设置缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(11));
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                LogUtil.d(TAG, " +++ onMarkerClick +++ ");
                mLocMarker = marker;
                chooseLocMarker = marker;
                return false;
            }
        });
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mLocMarker != null && mLocMarker.isInfoWindowShown())
                    mLocMarker.hideInfoWindow();//这个是隐藏infowindow窗口的方法
            }
        });

        //显示比例尺
        aMap.getUiSettings().setScaleControlsEnabled(true);
        aMap.setInfoWindowAdapter(this);
        // 声明定位回调监听器
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {

                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。

                        LogUtil.d(TAG, "  ====  location success  ====  " + amapLocation.getAdCode()
                                + "\r\n lat:" + amapLocation.getLatitude() + ",lon:" + amapLocation.getLongitude()
                                + ",city:" + amapLocation.getCity());

                        mapLocation = amapLocation;

                        getShopList(amapLocation.getLatitude() + ""
                                , amapLocation.getLongitude() + ""
                                , amapLocation.getCity());

                        //将地图移动到定位点
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(
                                new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));

                        addMyMarker();

                    } else {
//                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        LogUtil.e(TAG, " 定位失败 : getLocationInfo Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());

                        getShopList("0", "0", "北京市");
                    }
                }
            }
        };

        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
//        //低功耗定位模式：不会使用GPS和其他传感器，只会使用网络定位（Wi-Fi和基站定位）；
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
//        //仅用设备定位模式：不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位，自 v2.9.0 版本支持返回地址描述信息。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);

//        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
//        //获取最近3s内精度最高的一次定位结果：
//        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.setOnceLocationLatest(true);

        //SDK默认采用连续定位模式，时间间隔2000ms。如果您需要自定义调用间隔：
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
//        mLocationOption.setInterval(1000 * 60);//一分钟更新一次定位信息
//        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。设置是否强制刷新WIFI，默认为强制刷新。每次定位主动刷新WIFI模块会提升WIFI定位精度，但相应的会多付出一些电量消耗。
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置.设置是否允许模拟软件Mock位置结果，多为模拟GPS定位结果，默认为false，不允许模拟位置。
//        mLocationOption.setMockEnable(false);
        //设置定位请求超时时间，默认为30秒。单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(30000);
        //设置是否开启定位缓存机制 缓存机制默认开启，可以通过以下接口进行关闭。当开启定位缓存功能，在高精度模式和低功耗模式
        // 下进行的网络定位结果均会生成本地缓存，不区分单次定位还是连续定位。GPS定位结果不会被缓存。
        // 关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }

    private void getShopList(String lat, String lon, String city) {

        FragmentActivity activity = getActivity();
        if (activity instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) activity;

            if (!"0".equals(lat) && !"0".equals(lon))
                homeActivity.setLocation(lat, lon, city);

            if (firstLocation) {
                firstLocation = false;
                homeActivity.getShopList(Constant.PAGE_INDEX_BEGIN);
            }
        }

    }

    private void addMyMarker() {

        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        //myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
        myLocationStyle.showMyLocation(true);//控制是否显示定位蓝点
        //以下三种模式从5.1.0版本开始提供
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
//        Context context;
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_my_location);
        BitmapDescriptor myLocationIcon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_my_location);
        myLocationStyle.myLocationIcon(myLocationIcon);
        myLocationStyle.anchor(0.5f, 1.0f);
//        myLocationStyle.interval(1000 * 30);
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

    }

    //    private void addShopMarker(LatLng latlng, String title) {
    private void addShopMarker(ShopListEntity.DataBean dataBean) {

        if (dataBean == null)
            return;

        if (aMap == null)
            return;

        LatLng latLng = new LatLng(dataBean.getLatitude(), dataBean.getLongitude());

        String title = dataBean.getName() + "  "
                + CommUtils.getDecimalFormat2(dataBean.getDistance() / 1000f) + "km";

        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mark));
        options.anchor(0.5f, 1f);
        options.position(latLng);
        mLocMarker = aMap.addMarker(options);
        mLocMarker.setPeriod(MARKER_ID);
        mLocMarker.setObject(dataBean);
        mLocMarker.setTitle(title);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mapView != null)
            mapView.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null)
            mapView.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

        if (null != mLocationClient)
            mLocationClient.onDestroy();

    }

    @Override
    public View getInfoWindow(Marker marker) {

        return getMarkerView(marker);
    }

    @Override
    public View getInfoContents(Marker marker) {
        return getMarkerView(marker);

    }

    public View getMarkerView(Marker marker) {

        View windowView = getLayoutInflater().inflate(R.layout.layout_show_location, null);

        if (marker == null)
            return windowView;

        ShopListEntity.DataBean shopInfo = (ShopListEntity.DataBean) marker.getObject();

        TextView marker_title = (TextView) windowView.findViewById(R.id.marker_title);

        TextView nav = windowView.findViewById(R.id.nav);
        TextView sDetail = windowView.findViewById(R.id.sDetail);

        //LogUtil.d(TAG, "shopInfo::" + shopInfo.getName() + "," + shopInfo.getDistance());
        marker_title.setText(shopInfo.getName() + "  " + CommUtils.getDecimalFormat1(shopInfo.getDistance() / 1000) + "km");

        sDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getActivity(), ShopDetailActivity.class);
                mIntent.putExtra("cid", shopInfo.getCid());
                mIntent.putExtra("latitude", shopInfo.getLatitude() + "");
                mIntent.putExtra("longitude", shopInfo.getLongitude() + "");
                startActivity(mIntent);
            }
        });
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chooseLocMarker == null) {
                    showErr("请选择目的地");
                    return;
                }

                showChooseMap();
            }
        });

        return windowView;
    }


    public void setShopMap(List<ShopListEntity.DataBean> data, boolean clearOldData) {

        if (data == null || data.size() == 0)
            return;

        if (clearOldData) {

            List<Marker> mapScreenMarkers = aMap.getMapScreenMarkers();

            for (int i = 0; i < mapScreenMarkers.size(); i++) {

                Marker marker = mapScreenMarkers.get(i);

                int id = marker.getPeriod();
                LogUtil.d(TAG, "marker的id::" + id);

                if (MARKER_ID == id)
                    marker.remove();
            }

        }

        for (int i = 0; i < data.size(); i++) {
            ShopListEntity.DataBean dataBean = data.get(i);

            if (dataBean == null)
                continue;

            addShopMarker(dataBean);
        }

        ShopListEntity.DataBean dataBean = data.get(0);

        if (aMap == null || dataBean == null)
            return;

        aMap.moveCamera(CameraUpdateFactory.changeLatLng(
                new LatLng(dataBean.getLatitude(), dataBean.getLongitude())));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivMyLocation://回到我的位置

                back2myLocation();

                break;

        }
    }

    private void back2myLocation() {

        if (aMap == null || mapLocation == null)
            return;

        //将地图移动到定位点
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(
                new LatLng(mapLocation.getLatitude(), mapLocation.getLongitude())));
    }


    /**
     * 弹出选择照片的对话框
     */
    @Deprecated
    protected void showChooseMap() {

        if (getContext() == null)
            return;

        new ActionSheetDialog(getContext())
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("高德地图", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                // 高德地图

                                goToGaodeMap();

                            }

                        })
                .addSheetItem("百度地图", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                // 百度地图
                                goToBaiduMap();
                            }
                        })
                .show();

    }


    /**
     * 跳转百度地图
     */
    private void goToBaiduMap() {

        if (getActivity() == null)
            return;

        if (chooseLocMarker == null)
            return;

        if (!CommUtils.isAvilible(getActivity(), "com.baidu.BaiduMap")) {
            showErr("请先安装百度地图");
            return;
        }

        if (mapLocation == null)
            return;

        try {
            String uri = OpenLocalMapUtil.getBaiduMapUri(String.valueOf(mapLocation.getLatitude()), String.valueOf(mapLocation.getLongitude())
                    , mapLocation.getPoiName(),
                    String.valueOf(chooseLocMarker.getPosition().latitude)
                    , String.valueOf(chooseLocMarker.getPosition().longitude)
                    , chooseLocMarker.getTitle(), mapLocation.getCity(), "");
            Intent intent = Intent.parseUri(uri, 0);
            startActivity(intent); //启动调用
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 跳转高德地图
     */
    private void goToGaodeMap() {

        if (getActivity() == null)
            return;
        if (chooseLocMarker == null)
            return;

        if (!CommUtils.isAvilible(getActivity(), "com.autonavi.minimap")) {
            showErr("请先安装高德地图");
            return;
        }

        try {

            String uri = OpenLocalMapUtil.getGdMapUri(getResources().getString(R.string.app_name), "", "", ""
                    , String.valueOf(chooseLocMarker.getPosition().latitude)
                    , String.valueOf(chooseLocMarker.getPosition().longitude)
                    , chooseLocMarker.getTitle());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.autonavi.minimap");
            intent.setData(Uri.parse(uri));
            startActivity(intent); //启动调用

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
