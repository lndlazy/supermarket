package com.supermarket.haidilao.net;


import com.supermarket.haidilao.bean.Basebean;
import com.supermarket.haidilao.utils.LogUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseNoTObserver<T extends Basebean> implements Observer<T> {

    private static final String TAG = "BaseNoTObserver";

    @Override
    public void onNext(T t) {

        if (t.isSuccess())
            onHandleSuccess(t);
        else {
            onHandleError(t.getMessage());
            LogUtil.e("错误提示::" + t.getMessage());
        }
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e("  ******  onError  ******  ");
        onHandleError("网络错误 : " + e.getMessage());
    }


    @Override
    public void onComplete() {
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    public abstract void onHandleSuccess(T t);

    public abstract void onHandleError(String message);

//    public abstract void onReLogin(String message);

}