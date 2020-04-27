package com.supermarket.haidilao.utils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class CommUtils {

    public static boolean isPhone(String num) {

        if (TextUtils.isEmpty(num))
            return false;

        if (num.length() != 11)
            return false;

        return true;
    }

    /**
     * 保留2位小数
     */
    public static String getDecimalFormat2(float num) {

        return String.format("%.2f", num);
    }

    public static String getDecimalFormat1(float num) {

        return String.format("%.1f", num);
    }


    /**
     * 根据包名检测某个APP是否安装
     *
     * @param packageName 包名
     * @return true 安装 false 没有安装
     */
    /**
     * 检查手机上是否安装了指定的软件
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName){
        //获取packagemanager
        PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if(packageInfos != null){
            for(int i = 0; i < packageInfos.size(); i++){
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

}
