package com.supermarket.haidilao.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.supermarket.haidilao.R;
import com.supermarket.haidilao.base.BaseActivity;
import com.supermarket.haidilao.bean.LoginEntity;
import com.supermarket.haidilao.home.HomeActivity;
import com.supermarket.haidilao.net.BaseNoTObserver;
import com.supermarket.haidilao.net.RetrofitHttpUtil;
import com.supermarket.haidilao.utils.CommUtils;
import com.supermarket.haidilao.utils.LogUtil;
import com.supermarket.haidilao.utils.SaveUtils;
import com.supermarket.haidilao.utils.ScreenUtils;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    private EditText account;
    private Dialog errDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapplication.addA(this);

        LogUtil.d(TAG, "=====oncreate=====");
    }

    @Override
    protected void doWork() {

        long uid = SaveUtils.getLong(this, SaveUtils.KEY_UID);

        LogUtil.d(TAG, "uid::" + uid);

        if (uid != 0)
            nextActivityThenKill(HomeActivity.class);

    }

    @Override
    protected void setTitleBarColor() {
        LogUtil.d(TAG, "=====setTitleBarColor=====");

        StatusBarUtil.setColor(this, getResources().getColor(R.color.login_top_color));
    }

    @Override
    protected int getLayoutView() {
        LogUtil.d(TAG, "=====getLayoutView=====");

        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        account = findViewById(R.id.etAccount);
        TextView btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommUtils.isPhone(account.getText().toString())) {
                    login();
                } else {
                    showErr("请输入正确的手机号");
                }

            }
        });

        findViewById(R.id.tvZhengce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nextActivity(YinshiActivity.class);

            }
        });
    }


    /**
     * 显示 设置弹窗页
     */
    private void showSettingDialog(String message) {

        if (errDialog != null && errDialog.isShowing())
            return;

        errDialog = new Dialog(this, R.style.Dialog);
        errDialog.show();
        errDialog.setContentView(R.layout.dialog_login_err);
        ScreenUtils.screen_gray(this);
        errDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ScreenUtils.screen_light(LoginActivity.this);
            }
        });

        TextView note = errDialog.findViewById(R.id.note);
        note.setText(message);

    }


    private void login() {

        RetrofitHttpUtil.getInstance().login(new BaseNoTObserver<LoginEntity>() {
            @Override
            public void onHandleSuccess(LoginEntity loginEntity) {

                if (loginEntity != null) {

                    LoginEntity.DataBean entityData = loginEntity.getData();

                    if (entityData != null) {

                        long uid = entityData.getUid();

                        SaveUtils.putLong(LoginActivity.this, SaveUtils.KEY_UID, uid);
                        nextActivityThenKill(HomeActivity.class);
                    }

                }

            }

            @Override
            public void onHandleError(String message) {

                try {
                    showSettingDialog(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }, account.getText().toString());

    }
}
