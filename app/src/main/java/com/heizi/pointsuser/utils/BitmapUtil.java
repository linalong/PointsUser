package com.heizi.pointsuser.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.heizi.mycommon.utils.ImageFactory;

import static com.baidu.location.d.a.i;

/**
 * Created by leo on 17/7/8.
 */

public class BitmapUtil {

    private static Handler mHandler;

    public interface LoadCallback {
        public void onload(Bitmap bitmap);
    }


    public static void getBitmap(final Context context, final String url, final LoadCallback callback) {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                callback.onload((Bitmap) msg.obj);
            }
        };


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = ImageFactory.loadBitmap(url, 0, 0);
                    Message message = new Message();
                    message.what = i;
                    message.obj = ImageFactory.compressScale(bitmap, 80, 80);
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();
    }
}

