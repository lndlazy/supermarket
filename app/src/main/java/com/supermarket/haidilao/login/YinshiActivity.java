package com.supermarket.haidilao.login;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.supermarket.haidilao.R;
import com.supermarket.haidilao.base.BaseActivity;
import com.supermarket.haidilao.bean.InfoEntity;
import com.supermarket.haidilao.net.ApiService;
import com.supermarket.haidilao.net.BaseNoTObserver;
import com.supermarket.haidilao.net.RetrofitHttpUtil;

/**
 * 隐私政策
 */
public class YinshiActivity extends BaseActivity {

    private TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapplication.addA(this);

    }

    @Override
    protected void doWork() {

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_content = findViewById(R.id.tv_content);


        RetrofitHttpUtil.getInstance().getInfo(new BaseNoTObserver<InfoEntity>() {
            @Override
            public void onHandleSuccess(InfoEntity infoEntity) {


                if (infoEntity == null)
                    return;

                if (infoEntity.getData() == null || infoEntity.getData().size() == 0)
                    return;
                InfoEntity.DataBean dataBean = infoEntity.getData().get(0);
                if (dataBean == null)
                    return;

                String content = dataBean.getContent();
                tv_content.setText(Html.fromHtml(content));

            }

            @Override
            public void onHandleError(String message) {

            }

        }, ApiService.YS_TYPE);
    }

    @Override
    protected void setTitleBarColor() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.login_top_color));
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_yinshi;
    }

    @Override
    protected void initView() {

    }
}
