package com.supermarket.haidilao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.facebook.drawee.view.SimpleDraweeView;
import com.supermarket.haidilao.R;

import java.util.List;

public class SurroundAdapter extends RecyclerView.Adapter<SurroundAdapter.MyViewHolder> {

    private Context context;
    private List<String> picList;

    public SurroundAdapter(Context context, List<String> pics) {
        this.context = context;
        this.picList = pics;
    }


    @Override
    public SurroundAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SurroundAdapter.MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_surround, parent, false));
    }

    @Override
    public void onBindViewHolder(SurroundAdapter.MyViewHolder holder, int position) {

        holder.sdvImg.setImageURI(picList.get(position));
    }

    @Override
    public int getItemCount() {
        return picList == null ? 0 : picList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView sdvImg;

        MyViewHolder(View view) {
            super(view);
            sdvImg = view.findViewById(R.id.sdvSurround);

        }
    }
}