package com.supermarket.haidilao.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;


@SuppressLint("Registered")
public abstract class BaseActivity extends FragmentActivity {

    public Context mContext;
    protected Mapplication mapplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mapplication = (Mapplication) getApplication();

        if (getActionBar()!=null)
            getActionBar().hide();

        setContentView(getLayoutView());
        setTitleBarColor();
        initView();
        doWork();
    }

    protected abstract void doWork();

    protected abstract void setTitleBarColor();

    protected abstract int getLayoutView();

    protected abstract void initView();


    /**
     * 通过Class跳转界面
     **/
    public void nextActivity(Class<?> cls) {
        nextActivity(cls, null);

    }

    /**
     * nextActivity then finish
     * 跳转到下一个页面然后销毁当前页面
     */

    public void nextActivityThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }



    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void nextActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);

    }

    private void showToash(final String text) {

        try {
            if (!TextUtils.isEmpty(text))
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }


    public void showErr(final String msg) {

        if (Thread.currentThread() == Looper.getMainLooper().getThread())
            // 如果在主线程中
            showToash(msg);
        else
            // 在子线程中
            runOnUiThread(new Runnable() {
                public void run() {
                    showToash(msg);
                }
            });
    }


}
