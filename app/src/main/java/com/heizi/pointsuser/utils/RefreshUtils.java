package com.heizi.pointsuser.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.heizi.pointsuser.UserModel;

import static com.heizi.mycommon.Constants.action_refresh_login;


/**
 * Created by leo on 17/6/24.
 */

public class RefreshUtils {

    private refreshCallback callback;

    public interface refreshCallback {
        void call(UserModel userModel);
    }

    public void registerBoradcastReceiver(Context context, refreshCallback cb) {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(action_refresh_login);
        // 注册广播
        context.registerReceiver(mBroadcastReceiver, myIntentFilter);
        callback = cb;
    }

    public void unregisterBoradcastReceiver(Context context) {
        context.unregisterReceiver(mBroadcastReceiver);
    }


    /**
     * 刷新用户信息的广播
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(action_refresh_login)) {
                UserModel model = (UserModel) intent.getSerializableExtra("user");
                if (callback != null)
                    callback.call(model);
            }
        }

    };

    /**
     * 用户信息变更,发广播通知刷新
     *
     * @param context
     * @param userModel
     */
    public static void sendBroadcast(Context context, UserModel userModel) {
        Intent intent = new Intent();
        intent.setAction(action_refresh_login);
        intent.putExtra("user", userModel);
        context.sendBroadcast(intent);
    }
}
