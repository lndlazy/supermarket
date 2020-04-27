package com.supermarket.haidilao.base;

import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jaeger.library.StatusBarUtil;
import com.supermarket.haidilao.R;
import com.supermarket.haidilao.bean.InfoEntity;
import com.supermarket.haidilao.login.LoginActivity;
import com.supermarket.haidilao.net.ApiService;
import com.supermarket.haidilao.net.BaseNoTObserver;
import com.supermarket.haidilao.net.RetrofitHttpUtil;

import java.util.List;

public class SplishActivity extends BaseActivity {

    private SimpleDraweeView simpledrawView;

    private static final String TAG = "SplishActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mapplication.addA(this);
    }

    @Override
    protected void doWork() {

        RetrofitHttpUtil.getInstance().getInfo(new BaseNoTObserver<InfoEntity>() {

            @Override
            public void onHandleError(String message) {
                go2home();
            }


            @Override
            public void onHandleSuccess(InfoEntity infoEntity) {

                go2home();

                if (infoEntity != null) {

                    List<InfoEntity.DataBean> infoEntityData = infoEntity.getData();

                    if (infoEntityData != null && infoEntityData.size() > 0) {

                        InfoEntity.DataBean dataBean = infoEntityData.get(0);

                        String image = dataBean.getImage();

                        if (!TextUtils.isEmpty(image))
                            simpledrawView.setImageURI(Uri.parse(image));
                        simpledrawView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
                    }
                }
            }
        }, ApiService.START_UP_TYPE);

    }

    private void go2home() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                SystemClock.sleep(1000 * 3);

                nextActivityThenKill(LoginActivity.class);

            }
        }).start();
    }

    @Override
    protected void setTitleBarColor() {
        StatusBarUtil.setTransparent(this);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_splish;
    }

    @Override
    protected void initView() {

        simpledrawView = findViewById(R.id.simpledrawView);

    }
}
