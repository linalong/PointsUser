package com.heizi.mylibrary;

/**
 * 常量、接口等存放
 *
 * @author admin
 */
public interface Constants {


    final int PHOTO_PICKED_WITH_DATA = 3021;
    final int CAMERA_WITH_DATA = 3023;
    final int CAMERA_WITH_DATA_ZOOM = 3025;// 图片截取


    /**
     * 申请写sd卡权限
     */
    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 4000;

    // 通知刷新用户信息
    final String action_refresh_login = "UserRecord.refresh";
    // token失效
    final String action_token_error = "UserRecord.token.error";

    int NETWORK_NONE = 1;// 无网
    int NETWORK_WIFI = 2;// wifi
    int NETWORK_MOBILE = 3;// 移动网络


}