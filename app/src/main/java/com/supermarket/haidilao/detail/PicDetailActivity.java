package com.supermarket.haidilao.detail;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.supermarket.haidilao.R;
import com.supermarket.haidilao.base.BaseActivity;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

public class PicDetailActivity extends BaseActivity {

    private ArrayList<String> pics;
    private ViewPager viewpager;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        pics = getIntent().getStringArrayListExtra("pics");
        index = getIntent().getIntExtra("index", 0);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void doWork() {


    }

    @Override
    protected void setTitleBarColor() {

    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_pic_detail;
    }

    @Override
    protected void initView() {


        viewpager = findViewById(R.id.viewpager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, pics);
        viewpager.setAdapter(viewPagerAdapter);
        viewpager.setCurrentItem(index);

//        PhotoView photo_view = findViewById(R.id.photo_view);
//        photo_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });


    }
}
