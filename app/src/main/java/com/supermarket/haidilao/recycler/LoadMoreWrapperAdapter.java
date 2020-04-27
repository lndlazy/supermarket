//package com.supermarket.haidilao.recycler;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.supermarket.haidilao.R;
//
//import java.util.List;
//
//public class LoadMoreWrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private List<String> dataList;
//
//    public LoadMoreWrapperAdapter(List<String> dataList) {
//        this.dataList = dataList;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.adapter_recyclerview, parent, false);
//        return new RecyclerViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
//        recyclerViewHolder.tvItem.setText(dataList.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return dataList.size();
//    }
//
//    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
//
//        TextView tvItem;
//
//        RecyclerViewHolder(View itemView) {
//            super(itemView);
//            tvItem = (TextView) itemView.findViewById(R.id.tv_item);
//        }
//    }
//}