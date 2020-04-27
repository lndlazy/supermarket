package com.supermarket.haidilao.utils;

import android.app.Activity;
import android.view.WindowManager;

public class ScreenUtils {

    public static void screen_gray(Activity activity) {
        changeScreenLight(activity, 0.6f);
    }


    public static void screen_light(Activity activity) {
        changeScreenLight(activity, 1f);
    }

    private static void changeScreenLight(Activity activity, float value) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = value;
        activity.getWindow().setAttributes(lp);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

}
