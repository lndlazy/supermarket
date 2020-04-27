package com.supermarket.haidilao.my;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jaeger.library.StatusBarUtil;
import com.supermarket.haidilao.R;
import com.supermarket.haidilao.base.BaseActivity;
import com.supermarket.haidilao.bean.InfoEntity;
import com.supermarket.haidilao.bean.UserEntity;
import com.supermarket.haidilao.login.LoginActivity;
import com.supermarket.haidilao.net.ApiService;
import com.supermarket.haidilao.net.BaseNoTObserver;
import com.supermarket.haidilao.net.RetrofitHttpUtil;
import com.supermarket.haidilao.utils.FileHelper;
import com.supermarket.haidilao.utils.LogUtil;
import com.supermarket.haidilao.utils.SaveUtils;

import java.util.List;

public class MyActivity extends BaseActivity implements View.OnClickListener {


    private static final String TAG = "MyActivity";
    private static final int REQUEST_PERMISSION_CALL_CODE = 0x1212;
    private SimpleDraweeView headpic;
    private TextView name;
    private TextView account;
    private TextView pm;
    private TextView cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapplication.addA(this);

    }

    @Override
    protected void doWork() {

        getUserInfo();

        getPmInfo();

        setCacheSize();
    }

    private void setCacheSize() {
        try {
            String totalCacheSize = FileHelper.getTotalCacheSize(this);
            LogUtil.d(TAG, "缓存文件大小:::" + totalCacheSize);
            cache.setText(totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPmInfo() {

        RetrofitHttpUtil.getInstance().getInfo(new BaseNoTObserver<InfoEntity>() {
            @Override
            public void onHandleSuccess(InfoEntity infoEntity) {
                setPmInfo(infoEntity);
            }

            @Override
            public void onHandleError(String message) {

            }

        }, ApiService.PM_TYPE);

    }

    private void setPmInfo(InfoEntity infoEntity) {

        if (infoEntity == null)
            return;

        List<InfoEntity.DataBean> data = infoEntity.getData();

        if (data == null || data.size() == 0)
            return;

        InfoEntity.DataBean dataBean = data.get(0);

        if (dataBean == null)
            return;

        pm.setText(dataBean.getContent());

    }

    private void getUserInfo() {

        RetrofitHttpUtil.getInstance().userInfo(new BaseNoTObserver<UserEntity>() {
            @Override
            public void onHandleSuccess(UserEntity userEntity) {

                setUserInfo(userEntity);

            }

            @Override
            public void onHandleError(String message) {

            }

        }, SaveUtils.getLong(this, SaveUtils.KEY_UID));

    }

    private void setUserInfo(UserEntity userEntity) {

        if (userEntity == null)
            return;

        UserEntity.DataBean data = userEntity.getData();

        if (data == null)
            return;

        headpic.setImageURI(Uri.parse(data.getHeadImage()));
        name.setText(data.getRealName());
        account.setText(data.getPhone());

    }

    @Override
    protected void setTitleBarColor() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white));
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_my;
    }

    @Override
    protected void initView() {

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(this);

        headpic = findViewById(R.id.headpic);
        name = findViewById(R.id.name);
        account = findViewById(R.id.account);
        pm = findViewById(R.id.pm);
        cache = findViewById(R.id.cache);
        Button btnLoginOut = findViewById(R.id.btnLoginOut);
        ImageView ivclick = findViewById(R.id.ivclick);
//        RelativeLayout rl_log_off = findViewById(R.id.rl_log_off);

        btnLoginOut.setOnClickListener(this);
//        rl_log_off.setOnClickListener(this);
        cache.setOnClickListener(this);
        ivclick.setOnClickListener(this);

        pm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.back:
                finish();
                break;
            case R.id.btnLoginOut://退出登录

                //mapplication.exit();

                SaveUtils.putLong(MyActivity.this, SaveUtils.KEY_UID, 0L);
                mapplication.clearActivities();
                nextActivity(LoginActivity.class);

                break;

            case R.id.pm://人事电话
                checkCallPermission();
                break;

//            case R.id.rl_log_off://注销登录
//
//                SaveUtils.putLong(MyActivity.this, SaveUtils.KEY_UID, 0L);
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        SystemClock.sleep(500);
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mapplication.exit();
//
//                            }
//                        });
//
//                    }
//                }).start();

//                break;
            case R.id.ivclick://清除缓存
            case R.id.cache:

                FileHelper.clearAllCache(this);
                setCacheSize();

                break;
        }

    }


    private void checkCallPermission() {

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

        if (TextUtils.isEmpty(pm.getText().toString()))
            return;

        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage(pm.getText().toString()).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                //拨打电话
                Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + pm.getText().toString()));
                startActivity(intent);

            }
        }).show();
    }
}
