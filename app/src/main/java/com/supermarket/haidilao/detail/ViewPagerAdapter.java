package com.supermarket.haidilao.detail;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.supermarket.haidilao.R;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class ViewPagerAdapter extends PagerAdapter {
    private final Context context;
    private final List<String> list;

    public ViewPagerAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View inflate = LayoutInflater.from(context).inflate(R.layout.img_layout, null);
        PhotoView photoView = inflate.findViewById(R.id.photo_view);
        container.addView(inflate);

        photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        photoView.setImageURI(Uri.parse(list.get(position)));
//        ImageLoader.display(context,photoView,imgList[position]);
        Glide.with(context).load(list.get(position)).into(photoView);

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof PicDetailActivity) {
                    PicDetailActivity picDetailActivity = (PicDetailActivity) context;
                    picDetailActivity.finish();
                }
            }
        });

        return inflate;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}