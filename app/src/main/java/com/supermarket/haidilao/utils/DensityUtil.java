package com.supermarket.haidilao.utils;

import android.content.Context;

public class DensityUtil {

    /**
     *
     * @param context 上下文
     * @param dpValue dp数值
     * @return dp to  px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);

    }


    /**
     *
     * @param context    上下文
     * @param pxValue  px的数值
     * @return  px to dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);

    }

}
