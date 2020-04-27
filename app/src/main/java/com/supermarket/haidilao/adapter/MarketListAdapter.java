package com.supermarket.haidilao.adapter;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.supermarket.haidilao.R;
import com.supermarket.haidilao.bean.ShopListEntity;
import com.supermarket.haidilao.utils.CommUtils;
import com.supermarket.haidilao.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MarketListAdapter extends RecyclerView.Adapter<MarketListAdapter.MyViewHolder> {

    private static final String TAG = "MarketListAdapter";
//    private List<Integer> heightList;//随机数组
    private Context context;
    private List<ShopListEntity.DataBean> shopDataList;

    public MarketListAdapter(Context context, List<ShopListEntity.DataBean> shopDataList) {
        this.context = context;
        this.shopDataList = shopDataList;

//        //记录每个控件的随机高度,避免滑回到顶部出现空白
//        heightList = new ArrayList<>();
//        for (int i = 0; i < shopDataList.size(); i++) {
//            //创建随机高度
//            int height = new Random().nextInt(400) + 200;
//            heightList.add(height);
//        }

    }

    @Override
    public MarketListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MarketListAdapter.MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_market, parent, false));
    }

    @Override
    public void onBindViewHolder(MarketListAdapter.MyViewHolder holder, int position) {

        ShopListEntity.DataBean dataBean = shopDataList.get(position);

        if (dataBean == null)
            return;

        holder.sdvImg.setImageURI(Uri.parse(dataBean.getHeadImage()));
        holder.title.setText(dataBean.getName());
        holder.address.setText(dataBean.getAddress());
        holder.distance.setText(CommUtils.getDecimalFormat2(dataBean.getDistance()/1000f) + "km");
        holder.nearyest.setVisibility(position == 0 ? View.VISIBLE : View.INVISIBLE);
//        holder.detail.setText(dataBean.getIntroduce());

    }

    @Override
    public int getItemCount() {
        return shopDataList == null ? 0 : shopDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView sdvImg;
        TextView title;
        TextView address;
        TextView distance;
        TextView nearyest;
//        TextView detail;

        MyViewHolder(View view) {
            super(view);
            sdvImg = view.findViewById(R.id.sdv);
            title = view.findViewById(R.id.title);
            address = view.findViewById(R.id.address);
            distance = view.findViewById(R.id.distance);
            nearyest = view.findViewById(R.id.nearyest);
//            detail = view.findViewById(R.id.detail);
        }
    }
}