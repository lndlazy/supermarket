package com.supermarket.haidilao.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.supermarket.haidilao.R;
import com.supermarket.haidilao.adapter.MarketListAdapter;
import com.supermarket.haidilao.base.BaseFragment;
import com.supermarket.haidilao.bean.ShopListEntity;
import com.supermarket.haidilao.custom.OnItemClickListener;
import com.supermarket.haidilao.detail.ShopDetailActivity;
import com.supermarket.haidilao.utils.Constant;
import com.supermarket.haidilao.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ListFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private List<ShopListEntity.DataBean> shopDataList = new ArrayList<>();
    private MarketListAdapter marketListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(getContext(), R.layout.fragment_list, null);
        initView(view);
        return view;
    }

    public void setLocationList(List<ShopListEntity.DataBean> shopList, boolean clearOldData) {

        if (shopList == null)
            return;

        if (clearOldData)
            shopDataList.clear();

        shopDataList.addAll(shopList);

        if (marketListAdapter != null)
            marketListAdapter.notifyDataSetChanged();

    }


    private void initView(View view) {

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
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

//        // 通过 setEnabled(false) 禁用下拉刷新
//        swipeRefreshLayout.setEnabled(false);
//
//        // 设定下拉圆圈的背景
//        swipeRefreshLayout.setProgressBackgroundColor(getResources().getColor(R.color.colorPrimary));

        /*
         * 设置手势下拉刷新的监听
         */
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.shape_recyclerview_item_gray));
        recyclerView.addItemDecoration(divider);

        marketListAdapter = new MarketListAdapter(getActivity(), shopDataList);
        recyclerView.setAdapter(marketListAdapter);

        recyclerView.addOnItemTouchListener(new OnItemClickListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {

                Intent m = new Intent(getContext(), ShopDetailActivity.class);
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

                if (marketListAdapter != null && newState == RecyclerView.SCROLL_STATE_IDLE
                        && mPicLastVisibleItemPosition + 1 == marketListAdapter.getItemCount() && mPicFirstVisibleItemPosition > 0) {

                    LogUtil.d(TAG, "开始  加载第下一页");

                    FragmentActivity activity = getActivity();

                    if (activity instanceof HomeActivity) {
                        HomeActivity homeActivity = (HomeActivity) activity;

                        if (shopDataList.size() <= Constant.PAGE_INDEX_BEGIN * Constant.PAGE_SIZE) {
                            LogUtil.d(TAG, "真实  加载第下一页");

                            ++currentIndex;
                            homeActivity.getShopList(currentIndex);
                        }

                    }


                }

            }

        });
    }

    private int mPicFirstVisibleItemPosition;
    private int mPicLastVisibleItemPosition;

    private int currentIndex = Constant.PAGE_INDEX_BEGIN;

    @Override
    public void onRefresh() {

        // 刷新动画开始后回调到此方法
        LogUtil.d(TAG, "刷新数据");

        FragmentActivity activity = getActivity();

        if (activity instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) activity;
            currentIndex = Constant.PAGE_INDEX_BEGIN;
            homeActivity.getShopList(currentIndex);
        }

    }


    public void stopRefush() {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }
}
