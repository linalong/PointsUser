package com.heizi.mylibrary.retrofit2;

import android.content.Context;
import android.util.Log;


import com.heizi.mylibrary.Constants;
import com.heizi.mylibrary.ErrorConstants;
import com.heizi.mylibrary.callback.IResponseCallback;
import com.heizi.mylibrary.model.ErrorModel;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.PartMap;

public abstract class SimpleProtocol<T> implements Constants, ErrorConstants {
    protected WeakReference<IResponseCallback<T>> mTarget; //
    protected Context mContext;
    protected RetrofitClient2 retrofitClient;
    private BaseApiService apiService;
    private IResponseCallback<T> target;
    public static final String TAG = "retrofit==";

    public SimpleProtocol(Context context, String baseUrl) {
        this.mContext = context;
        this.retrofitClient = RetrofitClient2.getInstance(context, baseUrl);
        this.apiService = retrofitClient.createBaseApi();
    }

    public void getData(Map parameters, final IResponseCallback<T> callback) {

        this.mTarget = new WeakReference<IResponseCallback<T>>(callback);
        target = mTarget.get();
        if (target == null)
            return;

        Log.d(TAG, parameters.toString());
        callback.onStart();
        Call<String> call = apiService.executeGet(parameters);
        call.enqueue(getCallback());
    }


    public void postData(Map parameters, final IResponseCallback<T> callback) {

        this.mTarget = new WeakReference<IResponseCallback<T>>(callback);
        target = mTarget.get();
        if (target == null)
            return;

        callback.onStart();
        Call<String> call = apiService.executePost("", parameters);
        call.enqueue(getCallback());
    }


    public void uploadImgs(@PartMap() Map<String, RequestBody> maps, final IResponseCallback<T> callback) {

        this.mTarget = new WeakReference<IResponseCallback<T>>(callback);
        target = mTarget.get();
        if (target == null)
            return;

        callback.onStart();
        Call<String> call = apiService.upLoadFiles(maps);
        call.enqueue(getCallback());
    }


    private Callback<String> getCallback() {
        return new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "请求路径数据：" + call.request().url().toString() + "");
                if (target == null)
                    return;

                try {
                    JSONObject jsonResult = new JSONObject(response.body());
                    Log.d(TAG, "请求到得数据：" + jsonResult + "");
                    T t = parseJson(response.body());
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
//                    // token失效
//                    else if (jsonResult.getInt("code") == 100) {
//                        Intent mIntent = new Intent(
//                                MyApplication.action_token_error);
//                        mContext.sendBroadcast(mIntent);
//                    }

                } catch (org.json.JSONException e) {
                    Log.d(TAG, e.toString());
                    ErrorModel errorModel = new ErrorModel();
                    errorModel.setCode(JSONException);
                    errorModel.setMsg(JSONException_MSG);

                    target.onFailure(errorModel);
                }


            }


            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ErrorModel model = new ErrorModel();
                model.setMsg(t.getMessage().toString());
                target.onFailure(model);
            }
        };
    }


    protected abstract T parseJson(String jsonString);

}
