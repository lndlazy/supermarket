package com.supermarket.haidilao.home;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.jaeger.library.StatusBarUtil;
import com.supermarket.haidilao.R;
import com.supermarket.haidilao.base.BaseActivity;
import com.supermarket.haidilao.bean.ShopListEntity;
import com.supermarket.haidilao.my.MyActivity;
import com.supermarket.haidilao.net.BaseNoTObserver;
import com.supermarket.haidilao.net.RetrofitHttpUtil;
import com.supermarket.haidilao.utils.Constant;
import com.supermarket.haidilao.utils.LogUtil;

import java.util.List;


public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "HomeActivity";
    private static final int REQUEST_PERMISSION_LOCATION_CODE = 0x111;
    MapFragment mapFragment = new MapFragment();
    ListFragment listFragment = new ListFragment();
    private ImageView my;

    private String key = "";
    private String latitude = "0.0";
    private String longitude = "0.0";
    private int pageIndex = Constant.PAGE_INDEX_BEGIN;
    private EditText etKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapplication.addA(this);

    }


    @Override
    protected void doWork() {

        getPermission();
//        getShopList(pageIndex);
    }

    public void setLocation(String latitude, String longitude, String city) {
        this.latitude = latitude;
        this.longitude = longitude;

        mapplication.latitude = latitude;
        mapplication.longitude = longitude;
        mapplication.city = city;
    }


    public void getShopList(int pageIndex) {

        key = etKey.getText().toString();

        RetrofitHttpUtil.getInstance().shopList(new BaseNoTObserver<ShopListEntity>() {

            @Override
            public void onHandleSuccess(ShopListEntity shopListEntity) {

                if (listFragment != null)
                    listFragment.stopRefush();

                setShopInfo(shopListEntity, pageIndex);

            }

            @Override
            public void onHandleError(String message) {

                if (listFragment != null)
                    listFragment.stopRefush();
            }

        }, key, latitude, longitude, pageIndex + "", Constant.PAGE_SIZE + "");

    }

    private void setShopInfo(ShopListEntity shopListEntity, int pageIndex) {

        if (shopListEntity == null)
            return;

        List<ShopListEntity.DataBean> data = shopListEntity.getData();

        if (data == null)
            return;

        if (listFragment != null)
            listFragment.setLocationList(data, pageIndex == Constant.PAGE_INDEX_BEGIN);

        if (mapFragment != null)
            mapFragment.setShopMap(data, pageIndex == Constant.PAGE_INDEX_BEGIN);

    }

    @Override
    protected void setTitleBarColor() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white));
    }

    /**
     * 获取定位权限
     */
    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPer();
        }
    }

    /**
     * 检查定位权限， 获取定位信息
     */
    private void checkLocationPer() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //具有定位， 开始定位
            LogUtil.d("有定位权限，不需要再获取定位权限");
            startLocation();
        } else {
            //不具有定位权限，需要进行权限申请
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}
                    , REQUEST_PERMISSION_LOCATION_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case REQUEST_PERMISSION_LOCATION_CODE://定位权限

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "允许了定位权限，可以定位了");
                    startLocation();
                } else {
                    showErr("没有定位权限");
                    finish();
                    System.exit(0);
                }
                break;

        }

    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        ImageView my = findViewById(R.id.my);
        etKey = findViewById(R.id.etKey);
        TabLayout tab_layout = findViewById(R.id.tab_layout);
//        ViewPager viewPager = findViewById(R.id.viewPager);
        my.setOnClickListener(this);
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //选中某个tab
                chooseFragment(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //当tab从选择到未选择
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //已经选中tab后的重复点击tab
            }
        });

        etKey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                /*判断是否是“搜索”键*/
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    pageIndex = 1;
                    getShopList(pageIndex);

                    return true;
                }
                return false;
            }
        });

    }

    private void startLocation() {
        //定位权限获取成功
        LogUtil.d(TAG, "定位权限获取成功");
        showMap();
    }


    private void chooseFragment(TabLayout.Tab tab) {

        Log.d(TAG, "position::" + tab.getPosition());
        switch (tab.getPosition()) {

            case 0:

                showMap();

                break;
            case 1:

                if (listFragment == null)
                    listFragment = new ListFragment();
                FragmentTransaction transaction1 = switchFragment(listFragment);
                transaction1.commit();

                break;
        }
    }

    private void showMap() {
        if (mapFragment == null)
            mapFragment = new MapFragment();
        FragmentTransaction transaction = switchFragment(mapFragment);
        transaction.commit();
    }


    private Fragment currentFragment;

    private FragmentTransaction switchFragment(Fragment targetFragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下

            if (currentFragment != null)
                transaction.hide(currentFragment);

            transaction.add(R.id.fragment, targetFragment, targetFragment.getClass().getName());

        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;

    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    private long mPressedTime = 0;

    public void exitApp() {

        long mNowTime = System.currentTimeMillis();//获取第一次按键时间

        if ((mNowTime - mPressedTime) > 2000) {//比较两次按键时间差

            showErr("再按一次退出程序");

            mPressedTime = mNowTime;

        } else {//退出程序

            finishActivity();

        }
    }

    public void finishActivity() {
        try {
            mapplication.exit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.my:
                nextActivity(MyActivity.class);
                break;
        }
    }
}
