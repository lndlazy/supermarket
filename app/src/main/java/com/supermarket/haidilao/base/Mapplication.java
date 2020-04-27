package com.supermarket.haidilao.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.supermarket.haidilao.utils.LogUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Mapplication extends Application {

    private static final String TAG = "Mapplication";


    public String latitude = "0.0";
    public String longitude = "0.0";
    public String city = "";

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);

        String s = sHA1(getApplicationContext());
        LogUtil.d(TAG, "sha1值:" + s);
    }

    public String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
            }
            return hexString.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Activity> activityList = new ArrayList<>();

    //activity的集合
    public void addA(Activity activity) {
        if (activityList != null)
            activityList.add(activity);
    }


    public void clearActivities() {
        //退出应用
        for (int i = 0; i < activityList.size(); i++) {
            if (activityList.get(i) != null)
                activityList.get(i).finish();
        }

    }


    public void exit() {

        clearActivities();
        System.exit(0);

    }


}
