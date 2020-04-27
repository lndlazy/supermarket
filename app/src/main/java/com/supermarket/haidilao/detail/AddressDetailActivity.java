package com.supermarket.haidilao.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.jaeger.library.StatusBarUtil;
import com.supermarket.haidilao.R;
import com.supermarket.haidilao.base.BaseActivity;
import com.supermarket.haidilao.sheet.ActionSheetDialog;
import com.supermarket.haidilao.utils.CommUtils;
import com.supermarket.haidilao.utils.OpenLocalMapUtil;

public class AddressDetailActivity extends BaseActivity implements AMap.InfoWindowAdapter, View.OnClickListener {

    private MapView mapView;
    private String latitude;
    private String longitude;
    private String address;
    private String title;
    private AMap aMap;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        address = getIntent().getStringExtra("address");
        title = getIntent().getStringExtra("title");

        super.onCreate(savedInstanceState);

        mapView = findViewById(R.id.map);
        aMap = mapView.getMap();
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        mapplication.addA(this);
        ImageView back = findViewById(R.id.back);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        back.setOnClickListener(this);
        setMap();
        addShopMarker(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))
                , title);
    }

    private void setMap() {

        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setInfoWindowAdapter(this);
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mLocMarker != null && mLocMarker.isInfoWindowShown())
                    mLocMarker.hideInfoWindow();//这个是隐藏infowindow窗口的方法
            }
        });
        //设置缩放级别
    }

    @Override
    protected void doWork() {



    }


    private Marker mLocMarker;

    private void addShopMarker(LatLng latlng, String title) {

        if (aMap == null)
            return;

        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mark));
        options.anchor(0.5f, 1f);
        options.position(latlng);
        mLocMarker = aMap.addMarker(options);
        mLocMarker.setTitle(title);

//        //
        getInfoContents(mLocMarker);
        //移动到点
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(
                latlng));
    }



    @Override
    protected void setTitleBarColor() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white));
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_address_detail;
    }

    @Override
    protected void initView() {

        ImageView ivNav = findViewById(R.id.ivNav);
        ivNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showChooseMap();

            }
        });

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

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return getMarkerView(marker.getTitle());
    }

    @Override
    public View getInfoContents(Marker marker) {
        return getMarkerView(marker.getTitle());
    }


    public View getMarkerView(String title) {

        LayoutInflater inflater = getLayoutInflater();

        View windowView = inflater.inflate(R.layout.layout_show_location, null);
        TextView marker_title = (TextView) windowView.findViewById(R.id.marker_title);
        marker_title.setText(title);

        return windowView;
    }


    /**
     * 弹出选择照片的对话框
     */
    @Deprecated
    protected void showChooseMap() {

        new ActionSheetDialog(this)
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

        if (!CommUtils.isAvilible(this, "com.baidu.BaiduMap")) {
            showErr("请先安装百度地图");
            return;
        }

        try {
            String uri = OpenLocalMapUtil.getBaiduMapUri(mapplication.latitude, mapplication.longitude
                    , title, latitude, longitude
                    , title, mapplication.city, "");
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

        if (!CommUtils.isAvilible(this, "com.autonavi.minimap")) {
            showErr("请先安装高德地图");
            return;
        }

        try {

            String uri = OpenLocalMapUtil.getGdMapUri(getResources().getString(R.string.app_name), "", "", ""
                    , String.valueOf(latitude)
                    , String.valueOf(longitude)
                    , title);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setPackage("com.autonavi.minimap");
            intent.setData(Uri.parse(uri));
            startActivity(intent); //启动调用

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.back:
                finish();
                break;

        }
    }
}
