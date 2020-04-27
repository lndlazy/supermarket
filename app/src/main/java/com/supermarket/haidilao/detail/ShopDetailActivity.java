package com.supermarket.haidilao.detail;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jaeger.library.StatusBarUtil;
import com.supermarket.haidilao.R;
import com.supermarket.haidilao.adapter.ShopNearbyListAdapter;
import com.supermarket.haidilao.base.BaseActivity;
import com.supermarket.haidilao.bean.ShopDetailEntity;
import com.supermarket.haidilao.bean.ShopListEntity;
import com.supermarket.haidilao.custom.OnItemClickListener;
import com.supermarket.haidilao.home.HomeActivity;
import com.supermarket.haidilao.net.BaseNoTObserver;
import com.supermarket.haidilao.net.RetrofitHttpUtil;
import com.supermarket.haidilao.utils.CommUtils;
import com.supermarket.haidilao.utils.Constant;
import com.supermarket.haidilao.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class ShopDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ShopDetailActivity";

    private static final int REQUEST_PERMISSION_CALL_CODE = 0x333;

    private String cid;
    String latitude = "0.0";
    String longitude = "0.0";

    private RecyclerView recyclerView;

    private String telphone;

    private SwipeRefreshLayout swipeRefreshLayout;

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        cid = getIntent().getStringExtra("cid");
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        super.onCreate(savedInstanceState);

        mapplication.addA(this);
    }

    @Override
    protected void doWork() {

//        getShopDetail();

        currentIndex = 1;
        getNearbyList();
    }


    /**
     * 获取附近的商品列表
     */
    private void getNearbyList() {

        RetrofitHttpUtil.getInstance().nearbyList(new BaseNoTObserver<ShopListEntity>() {
            @Override
            public void onHandleSuccess(ShopListEntity nearbyList) {

                stopRefush();

                if (nearbyList == null || !nearbyList.isSuccess()) {
                    showErr("获取数据失败");
                    return;
                }

                if (currentIndex == 1) {
                    shopDataList.clear();
                }

                shopDataList.addAll(nearbyList.getData());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onHandleError(String message) {
                stopRefush();

                showErr("获取附近商家失败");
            }

        }, latitude, longitude, currentIndex + "", Constant.PAGE_SIZE + "");
    }


    @Override
    protected void setTitleBarColor() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white));
    }


    @Override
    protected int getLayoutView() {
        return R.layout.activity_shop_nearby_list;
//        return R.layout.activity_shop_detail;
    }


    private ShopNearbyListAdapter adapter;
    private List<ShopListEntity.DataBean> shopDataList = new ArrayList<>();

    @Override
    protected void initView() {

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        // 设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
        swipeRefreshLayout.setProgressViewOffset(true, 50, 150);

        // 设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);

        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        /*
         * 设置手势下拉刷新的监听
         */
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = findViewById(R.id.shopRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.shape_recyclerview_item_gray));
        recyclerView.addItemDecoration(divider);
        adapter = new ShopNearbyListAdapter(this, shopDataList, cid, latitude, longitude);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.backHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m = new Intent(ShopDetailActivity.this, HomeActivity.class);
                startActivity(m);
            }
        });

        recyclerView.addOnItemTouchListener(new OnItemClickListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {

                if (position==0)
                    return;

                --position;

                LogUtil.d(TAG, "查看详情？？？ ");
                Intent m = new Intent(ShopDetailActivity.this, ShopDetailActivity.class);
                m.putExtra("cid", shopDataList.get(position).getCid());
                m.putExtra("latitude", shopDataList.get(position).getLatitude() + "");
                m.putExtra("longitude", shopDataList.get(position).getLongitude() + "");
                startActivity(m);
//                //查看详情
//                nextActivity(ShopDetailActivity.class);

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                if (layoutManager instanceof LinearLayoutManager) {
                    mPicLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    mPicFirstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                }

                if (adapter != null && newState == RecyclerView.SCROLL_STATE_IDLE
                        && mPicLastVisibleItemPosition + 1 == adapter.getItemCount() - 1 && mPicFirstVisibleItemPosition > 0) {

                    LogUtil.d(TAG, "开始  加载第下一页");

//                    FragmentActivity activity = getActivity();
//
//                    if (activity instanceof HomeActivity) {
//                        HomeActivity homeActivity = (HomeActivity) activity;
//
                    if (shopDataList.size() <= Constant.PAGE_INDEX_BEGIN * Constant.PAGE_SIZE) {
                        LogUtil.d(TAG, "真实  加载第下一页");

                        ++currentIndex;
                        getNearbyList();
                    }
//
//                    }


                }

            }

        });
    }


    private int mPicFirstVisibleItemPosition;
    private int mPicLastVisibleItemPosition;

    private int currentIndex = Constant.PAGE_INDEX_BEGIN;

    public void checkCallPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            //有权限
            call();
        } else {
            //不具有定位权限，需要进行权限申请
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL_CODE);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case REQUEST_PERMISSION_CALL_CODE://定位权限

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    call();
                else
                    showErr("没有拨号权限");

                break;

        }

    }


    private void call() {

        if (TextUtils.isEmpty(telphone))
            return;

        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage(telphone).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                //拨打电话
                Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + telphone));
                startActivity(intent);

            }
        }).show();
    }

    @Override
    public void onRefresh() {
        currentIndex = Constant.PAGE_INDEX_BEGIN;
        getNearbyList();
    }

    public void stopRefush() {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }

}
