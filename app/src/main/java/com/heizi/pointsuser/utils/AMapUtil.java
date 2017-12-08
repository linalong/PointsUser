package com.heizi.pointsuser.utils;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by leo on 17/9/20.
 */

public class AMapUtil {

    private static AMapLocationClient mlocationClient;
    private static AMapLocationClientOption mLocationOption;
    private static AMapLocationListener preListener;

    public static void startLocation(Context context, AMapLocationListener listener) {
        if (mlocationClient == null || !mlocationClient.isStarted()) {
            mlocationClient = new AMapLocationClient(context);
            mLocationOption = new AMapLocationClientOption();
            if (preListener != null) {
                mlocationClient.unRegisterLocationListener(preListener);
            }
            mlocationClient.setLocationListener(listener);
            preListener = listener;
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(2000);
            mlocationClient.setLocationOption(mLocationOption);
            mlocationClient.startLocation();
        } else {
            if (mlocationClient.isStarted()) {
                mlocationClient.stopLocation();
            } else {
            }
        }
    }
}
