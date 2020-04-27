package com.supermarket.haidilao.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.facebook.drawee.view.SimpleDraweeView;
import com.supermarket.haidilao.R;
import com.supermarket.haidilao.base.Mapplication;
import com.supermarket.haidilao.bean.ShopDetailEntity;
import com.supermarket.haidilao.bean.ShopListEntity;
import com.supermarket.haidilao.custom.SpaceItemDecoration;
import com.supermarket.haidilao.detail.AddressDetailActivity;
import com.supermarket.haidilao.detail.PicDetailActivity;
import com.supermarket.haidilao.detail.ShopDetailActivity;
import com.supermarket.haidilao.net.BaseNoTObserver;
import com.supermarket.haidilao.net.RetrofitHttpUtil;
import com.supermarket.haidilao.utils.CommUtils;
import com.supermarket.haidilao.utils.DensityUtil;
import com.supermarket.haidilao.utils.LogUtil;
import com.supermarket.haidilao.custom.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class ShopNearbyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final String TAG = "ShopNearbyListAdapter";

    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_BOTTOM = 2;

    private int mHeaderCount = 1;//头部View个数
    private int mBottomCount = 0;//底部View个数

    private LayoutInflater mLayoutInflater;
    //    private List<Integer> heightList;//随机数组
    private Context context;
    private List<ShopListEntity.DataBean> shopDataList;
    String latitude = "0.0";
    String longitude = "0.0";
    private String cid;


    public ShopNearbyListAdapter(Context context, List<ShopListEntity.DataBean> shopDataList, String cid, String lat, String ln) {
        this.context = context;
        this.cid = cid;
        this.latitude = lat;
        this.longitude = ln;
        mLayoutInflater = LayoutInflater.from(context);
        this.shopDataList = shopDataList;

    }

//    //内容长度
//    public int getContentItemCount() {
//        return shopDataList==null? 0 : shopDataList.size();
//    }


    //判断当前item类型
    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return ITEM_TYPE_HEADER;

        return ITEM_TYPE_CONTENT;

    }

    //    private String telphone;
    private String headImage;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.sdv://店铺头像

                Intent m = new Intent(context, PicDetailActivity.class);
                ArrayList<String> pics = new ArrayList<>();
                pics.add(headImage);
                m.putStringArrayListExtra("pics", pics);
                m.putExtra("index", 0);
                context.startActivity(m);
                break;

//            case R.id.back:
//
//                if (context instanceof ShopDetailActivity)
//                    ((ShopDetailActivity) context).finish();
//                break;

            case R.id.lookLocation://查看位置

                if ("0.0".equals(latitude) && "0.0".equals(longitude)) {
                    if (context instanceof ShopDetailActivity)
                        ((ShopDetailActivity) context).showErr("未获取到地址");

                    return;
                }

                Intent n = new Intent(context, AddressDetailActivity.class);
                n.putExtra("latitude", latitude);
                n.putExtra("longitude", longitude);
                n.putExtra("address", currentAddress);
                n.putExtra("title", currentTitle);
                context.startActivity(n);

                break;

            case R.id.tel://拨打电话
            case R.id.ivcall:

                ((ShopDetailActivity) context).checkCallPermission();

                break;
        }
    }


    //内容 ViewHolder
    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView sdvImg;
        TextView title;
        TextView address;
        TextView distance;
        TextView nearyest;


        public ContentViewHolder(View view) {
            super(view);
            sdvImg = view.findViewById(R.id.sdv);
            title = view.findViewById(R.id.title);
            address = view.findViewById(R.id.address);
            distance = view.findViewById(R.id.distance);
            nearyest = view.findViewById(R.id.nearyest);

        }
    }


    //底部 ViewHolder
    public static class BottomViewHolder extends RecyclerView.ViewHolder {

        public BottomViewHolder(View itemView) {
            super(itemView);
        }
    }

    //头部 ViewHolder
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        private SimpleDraweeView simpledrawView;
        private TextView title;
        private TextView introduce;
        private TextView address;
        private TextView distance;
        private TextView lookLocation;
        private TextView shopContact;
        private ImageView ivcall;

        private ImageView iv_no_data;
        private TextView tv_no_data;

        public HeaderViewHolder(View view) {
            super(view);

            recyclerView = view.findViewById(R.id.recyclerView);
            ivcall = view.findViewById(R.id.ivcall);
            simpledrawView = view.findViewById(R.id.simpledrawView);
            title = view.findViewById(R.id.title);
            introduce = view.findViewById(R.id.introduce);
            address = view.findViewById(R.id.address);
            distance = view.findViewById(R.id.distance);
            lookLocation = view.findViewById(R.id.lookLocation);
            shopContact = view.findViewById(R.id.shopContact);

            iv_no_data = view.findViewById(R.id.iv_no_data);
            tv_no_data = view.findViewById(R.id.tv_no_data);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            return new HeaderViewHolder(mLayoutInflater.inflate(R.layout.activity_shop_detail, parent, false));
        } else if (viewType == ITEM_TYPE_CONTENT) {
            return new ContentViewHolder(mLayoutInflater.inflate(R.layout.item_market, parent, false));
        } else if (viewType == ITEM_TYPE_BOTTOM) {
//            return new BottomViewHolder(mLayoutInflater.inflate(R.layout.rv_footer, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeaderViewHolder) {

            setHeadInfo(holder);

        } else if (holder instanceof ContentViewHolder) {

            --position;
            ShopListEntity.DataBean dataBean = shopDataList.get(position);

            if (dataBean == null)
                return;

            ((ContentViewHolder) holder).nearyest.setVisibility(View.GONE);
            ((ContentViewHolder) holder).address.setText(dataBean.getAddress());
            ((ContentViewHolder) holder).distance.setText(CommUtils.getDecimalFormat2(dataBean.getDistance() / 1000f) + "km");
            ((ContentViewHolder) holder).sdvImg.setImageURI(Uri.parse(dataBean.getHeadImage()));
            ((ContentViewHolder) holder).title.setText(dataBean.getName());

        } else if (holder instanceof BottomViewHolder) {

        }
    }


    /**
     * 列数（可在这里改变列数）
     * 如果要分割线均等，得自己去SpaceItemDecoration类判断，这边我只写了两列为例子
     */
    public static final int spanCount = 2;
    /**
     * 分割线
     */
    private int space;
    private SurroundAdapter surroundAdapter;
    private List<String> picList = new ArrayList<>();
    private ArrayList<String> imageList;

    private void setHeadInfo(RecyclerView.ViewHolder holder) {

        space = DensityUtil.px2dip(context, 64);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        getShopDetail(holder);

        RecyclerView recyclerView = ((HeaderViewHolder) holder).recyclerView;
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置分割线
        if (recyclerView.getItemDecorationCount() == 0) {
            recyclerView.addItemDecoration(new SpaceItemDecoration(space, spanCount));
        }
        surroundAdapter = new SurroundAdapter(context, picList);
        recyclerView.setAdapter(surroundAdapter);

        ((HeaderViewHolder) holder).lookLocation.setOnClickListener(this);
        ((HeaderViewHolder) holder).ivcall.setOnClickListener(this);

        ((HeaderViewHolder) holder).simpledrawView.setOnClickListener(this);
        recyclerView.addOnItemTouchListener(new OnItemClickListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {

                Intent m = new Intent(context, PicDetailActivity.class);
                m.putExtra("pics", imageList);
                m.putExtra("index", position);
                context.startActivity(m);

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


    }


    /**
     * 获取商店详情
     *
     * @param holder
     */
    private void getShopDetail(RecyclerView.ViewHolder holder) {

        Mapplication mapplication = (Mapplication) ((ShopDetailActivity) context).getApplication();

        RetrofitHttpUtil.getInstance().shopDetail(new BaseNoTObserver<ShopDetailEntity>() {
            @Override
            public void onHandleSuccess(ShopDetailEntity shopDetailEntity) {

                if (shopDetailEntity != null)
                    setShopInfo(shopDetailEntity, holder);

            }

            @Override
            public void onHandleError(String message) {

            }

        }, cid, mapplication.latitude, mapplication.longitude);

    }


    String currentAddress;
    String currentTitle;

    private void setShopInfo(ShopDetailEntity shopDetailEntity, RecyclerView.ViewHolder holder) {

        ShopDetailEntity.DataBean data = shopDetailEntity.getData();

        if (data == null)
            return;
        headImage = data.getHeadImage();
        LogUtil.d(TAG, "headImage::" + headImage);
        if (!TextUtils.isEmpty(headImage))
            ((HeaderViewHolder) holder).simpledrawView.setImageURI(Uri.parse(headImage));
        ((HeaderViewHolder) holder).title.setText(data.getName());
        ((HeaderViewHolder) holder).introduce.setText(data.getIntroduce());
        ((HeaderViewHolder) holder).distance.setText(CommUtils.getDecimalFormat2(data.getDistance() / 1000f) + "km");
        ((HeaderViewHolder) holder).address.setText(data.getAddress());
        currentAddress = data.getAddress();
        currentTitle = data.getName();
        if (TextUtils.isEmpty(data.getContactName()) || TextUtils.isEmpty(data.getContactPhone())) {

            ((HeaderViewHolder) holder).shopContact.setVisibility(View.GONE);
            ((HeaderViewHolder) holder).ivcall.setVisibility(View.GONE);

        } else {

            ((HeaderViewHolder) holder).shopContact.setVisibility(View.VISIBLE);
            ((HeaderViewHolder) holder).ivcall.setVisibility(View.VISIBLE);
            ((HeaderViewHolder) holder).shopContact.setText(data.getContactName());
            ((ShopDetailActivity) context).setTelphone(data.getContactPhone());

        }

        imageList = data.getImageList();

        if (imageList == null || imageList.size() == 0) {

            ((HeaderViewHolder) holder).recyclerView.setVisibility(View.GONE);
            ((HeaderViewHolder) holder).iv_no_data.setVisibility(View.VISIBLE);
            ((HeaderViewHolder) holder).tv_no_data.setVisibility(View.VISIBLE);

        } else {

            ((HeaderViewHolder) holder).recyclerView.setVisibility(View.VISIBLE);
            ((HeaderViewHolder) holder).iv_no_data.setVisibility(View.GONE);
            ((HeaderViewHolder) holder).tv_no_data.setVisibility(View.GONE);
            ((HeaderViewHolder) holder).recyclerView.setVisibility(View.VISIBLE);

            picList.clear();
            picList.addAll(imageList);
            surroundAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public int getItemCount() {
        return shopDataList == null ? 1 : shopDataList.size() + 1;
    }

}