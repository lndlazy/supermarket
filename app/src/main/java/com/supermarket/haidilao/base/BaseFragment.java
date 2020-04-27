package com.supermarket.haidilao.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {


//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return getView();
//    }
//
//    public abstract View getView();


    public void showErr(final String msg) {

        if (Thread.currentThread() == Looper.getMainLooper().getThread())
            // 如果在主线程中
            showToash(msg);
        else
            // 在子线程中
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    showToash(msg);
                }
            });
    }

    /**
     * 通过Class跳转界面
     **/
    public void nextActivity(Class<?> cls) {
        nextActivity(cls, null);

    }



    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void nextActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getContext(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);

    }

    private void showToash(final String text) {

        try {
            if (!TextUtils.isEmpty(text))
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
