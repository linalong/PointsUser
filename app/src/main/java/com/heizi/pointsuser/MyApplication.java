package com.heizi.pointsuser;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;

import com.heizi.mycommon.utils.ImageFactory;
import com.heizi.pointsuser.block.home.ModelAddress;
import com.heizi.pointsuser.service.LocationService;
import com.heizi.pointsuser.utils.DbUtils;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;


/**
 * 应用程序 类
 */
public class MyApplication extends Application implements Constants {

    // 百度定位
    public LocationService locationService;// 百度地图
    private UserModel userModel;
    public ModelAddress modelAddress;
    // private LoginProtocol loginProtocol;
    // private IResponseCallback<com.heizi.pointsuser.UserModel> callback;
    private boolean isBusy = false;
    // 实例
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    {

        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // 处理系统异常（崩溃后不显示错误弹出框）
        // CrashHandler crashHandler = CrashHandler.getInstance();
        // crashHandler.init(getApplicationContext());
        // initLocationClient();
        locationService = new LocationService(getApplicationContext());

        instance = this;

        x.Ext.init(this);


        ImageFactory.initImageLoader(this);
        /**
         * 解决7.0拍照闪退
         */
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        UMShareAPI.get(this);


        JPushInterface.init(this);
        JPushInterface.setDebugMode(true);
    }

    @TargetApi(23)
    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
            DbUtils.sava(userModel);
        }
    }


    public UserModel getUserModel() {
        return userModel;
    }

    @TargetApi(23)
    public UserModel refreshUserModel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
            userModel = DbUtils.get();
        }

        return userModel;
    }


    public boolean tt() {
        if (userModel != null && userModel.getUser_state().equals("1"))
            return true;
        else
            return false;
    }

    public boolean rr() {
        if (userModel != null)
            return true;
        else
            return false;
    }

    @TargetApi(23)
    public void logout() {
        userModel = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
            DbUtils.deleteUser();
        }
    }


    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }


    // private void initData() {
    // loginProtocol = new LoginProtocol(instance);
    // callback = new IResponseCallback<com.heizi.pointsuser.UserModel>() {
    //
    // @Override
    // public void onSuccess(com.heizi.pointsuser.UserModel userModel) {
    // // TODO Auto-generated method stub
    // isBusy = false;
    // com.heizi.pointsuser.UserModel um = userRecord.getUser();
    // userModel.setUser_id(um.getUser_id());// id不更新
    // // userModel.setAvatar(um.getAvatar());//
    // userRecord.setUser(userModel);
    // userRecord.setUserName(userModel.getUser_name());
    // userRecord.setPwd(userModel.getPassword());
    // userRecord.save(instance);
    //
    // // 存储成功发出刷新activity广播
    // Intent mIntent = new Intent(
    // com.heizi.pointsuser.MyApplication.action_refresh_complete);
    // sendBroadcast(mIntent);
    // }
    //
    // @Override
    // public void onStart() {
    // // TODO Auto-generated method stub
    // isBusy = true;
    // }
    //
    // @Override
    // public void onFailure(ErrorModel errorModel) {
    // // TODO Auto-generated method stub
    // isBusy = false;
    // }
    // };
    // }
    //
    // private void getUserInfo() {
    // if (isBusy)
    // return;
    // if (userRecord != null && !Utils.isNull(userRecord.getUserName())
    // && !Utils.isNull(userRecord.getPwd())) {
    // RequestParams params = new RequestParams();
    // params.addQueryStringParameter("service", "User.Info");
    // params.addQueryStringParameter("token", userRecord.getUser()
    // .getUser_token());
    // loginProtocol
    // .getData(HttpMethod.POST, SERVER_URL, params, callback);
    // }
    // }
    //


}
