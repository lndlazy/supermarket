package com.supermarket.haidilao.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by linaidao on 2018/10/16.
 */

public class SaveUtils {

    
    private static final String CONFIG = "supermarket_config";

    public static final String KEY_UID = "key_uid";
    
    public static void putInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    /**
     * 获取缓存中的int值，默认-1
     *
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sp.getInt(key, -1);
    }


    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public static void putLong(Context context, String key, long value) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        sp.edit().putLong(key, value).apply();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static String getStringWithDefault(Context context, String key,String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    /**
     * 获取缓存中的long值，默认0
     *
     * @param context
     * @param key
     * @return
     */
    public static long getLong(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sp.getLong(key, 0);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    /**
     * 获取缓存中的boolean值
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }


}
