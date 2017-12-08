package com.heizi.mylibrary.xutils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.heizi.mylibrary.Constants;
import com.heizi.mylibrary.ErrorConstants;
import com.heizi.mylibrary.callback.IResponseCallback;
import com.heizi.mylibrary.model.ErrorModel;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;


public abstract class BaseProtocol<T> implements Constants, ErrorConstants {
    protected WeakReference<IResponseCallback<T>> mTarget; //
    protected String cacheUrl;
    protected Context context;
    protected HttpUtils http;
    private IResponseCallback<T> target;
    public static final String TAG = "====";

    public BaseProtocol(Context context) {
        this.context = context;
    }

    public void getData(HttpMethod method, String url, RequestParams params,
                        IResponseCallback<T> callback) {
        this.mTarget = new WeakReference<IResponseCallback<T>>(callback);
        if (params != null) {
            List<NameValuePair> lparams = params.getQueryStringParams();
            if (lparams != null)
                cacheUrl = url + "?" + URLEncodedUtils.format(lparams, "UTF-8");
        }
        Log.d("====", "" + cacheUrl);
        target = mTarget.get();
        if (target == null)
            return;
        // String cacheConfigString =
        // ConfigCache.getUrlCache(context,ConfigCache.getCacheDecodeString(cacheUrl));
        // if (cacheConfigString != null) {
        // target.onSuccess(parseJson(cacheConfigString)); //通知更新UI
        // UtilsLog.w("====", "缓存===="+parseJson(cacheConfigString));
        // return;
        // }
        http = new HttpUtils(15000);
        http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                if (target == null)
                    return;

                try {
                    JSONObject jsonResult = new JSONObject(responseInfo.result);
                    Log.d(TAG, "请求到得数据：" + jsonResult + "");
                    // 请求成功
                    if (jsonResult.getInt("ret") == 200) {
                        T t = parseJson(responseInfo.result);
                        if (t != null) {
                            // 成功

                            target.onSuccess(t);
                        } else {
                            // 解析失败

                            ErrorModel errorModel = new ErrorModel();
                            errorModel.setCode(JSONException);
                            errorModel.setMsg(JSONException_MSG);
                            target.onFailure(errorModel);
                        }
                    }
                    // token失效
                    else if (jsonResult.getInt("ret") == 100) {
                        Intent mIntent = new Intent(
                                action_token_error);
                        context.sendBroadcast(mIntent);
                    } else {
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setMsg("非法操作");
                        errorModel.setCode(jsonResult.getInt("ret"));
                        Log.d(TAG, jsonResult.getString("msg"));
                        target.onFailure(errorModel);
                    }

                } catch (JSONException e) {
                    Log.d(TAG, e.toString());
                    ErrorModel errorModel = new ErrorModel();
                    errorModel.setCode(JSONException);
                    errorModel.setMsg(JSONException_MSG);

                    target.onFailure(errorModel);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (target == null)
                    return;
                ErrorModel errorModel = new ErrorModel();
                errorModel.setCode(error.getExceptionCode());
                errorModel.setMsg(msg);
                target.onFailure(errorModel);

            }

            @Override
            public void onStart() {
                super.onStart();
                if (target == null)
                    return;
                target.onStart();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                if (target == null)
                    return;
                target.onLoading(total, current, isUploading);
            }
        });
    }

    protected abstract T parseJson(String jsonString);

    // 广播发送(未登录时广播)
    private void sendBroadcast() {
        Intent intent = new Intent("no_login");
        intent.putExtra("no_login", "no_login");
        context.sendBroadcast(intent);
    }
}
