package com.heizi.mylibrary.retrofit2;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.heizi.mylibrary.Constants;
import com.heizi.mylibrary.ErrorConstants;
import com.heizi.mylibrary.callback.IResponseCallback;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * RetrofitClient
 * Created by Tamic on 2016-06-15.
 * {@link # https://github.com/NeglectedByBoss/RetrofitClient}
 */
public abstract class RetrofitClient<T> implements Constants, ErrorConstants {

    private static final int DEFAULT_TIMEOUT = 20;
    private BaseApiService apiService;
    private static OkHttpClient okHttpClient;
    public static String baseUrl = "https://www.baidu.com/";
    private static Context mContext;
    private static RetrofitClient sNewInstance;

    private static Retrofit retrofit;
    private Cache cache = null;
    private File httpCacheDirectory;


    protected WeakReference<IResponseCallback<T>> mTarget; //
    private IResponseCallback<T> target;

    protected abstract T parseJson(String jsonString);


    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(baseUrl);
    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder()
                    .addNetworkInterceptor(
                            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);


    private RetrofitClient() {

    }

    public RetrofitClient(Context context) {

        this(context, baseUrl, null);
        createBaseApi();
    }

    private RetrofitClient(Context context, String url) {

        this(context, url, null);

        createBaseApi();
    }

    private RetrofitClient(Context context, String url, Map<String, String> headers) {
        mContext = context;
        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }

        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(mContext.getCacheDir(), "tamic_cache");
        }

        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
            }
        } catch (Exception e) {
            Log.e("OKHttp", "Could not create http cache", e);
        }
        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .cookieJar(new NovateCookieManger(context))
                .cache(cache)
                .addInterceptor(new BaseInterceptor(headers))
                .addInterceptor(new CaheInterceptor(context))
                .addNetworkInterceptor(new CaheInterceptor(context))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();

    }


    public void get(String url, Map parameters, final IResponseCallback<T> callback) {

//        this.mTarget = new WeakReference<IResponseCallback<T>>(callback);
//        target = mTarget.get();
//        if (target == null)
//            return;
//
//        callback.onStart();
//        Call<String> call = apiService.executeGet(url, parameters);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (target == null)
//                    return;
//
//                try {
//                    JSONObject jsonResult = new JSONObject(response.body());
//                    UtilsLog.d("====", "请求到得数据：" + jsonResult + "");
//                    // 请求成功
//                    if (jsonResult.getInt("code") == 1) {
//                        T t = parseJson(response.body());
//                        if (t != null) {
//                            // 成功
//
//                            target.onSuccess(t);
//                        } else {
//                            // 解析失败
//
//                            ErrorModel errorModel = new ErrorModel();
//                            errorModel.setCode(JSONException);
//                            errorModel.setMsg(JSONException_MSG);
//                            target.onFailure(errorModel);
//                        }
//                    }
//                    // token失效
//                    else if (jsonResult.getInt("code") == 100) {
//                        Intent mIntent = new Intent(
//                                MyApplication.action_token_error);
//                        mContext.sendBroadcast(mIntent);
//                    } else {
//                        ErrorModel errorModel = new ErrorModel();
//                        errorModel.setMsg("非法操作");
//                        errorModel.setCode(jsonResult.getInt("ret"));
//                        UtilsLog.d(TAG, jsonResult.getString("msg"));
//                        target.onFailure(errorModel);
//                    }
//
//                } catch (JSONException e) {
//                    UtilsLog.d(TAG, e.toString());
//                    ErrorModel errorModel = new ErrorModel();
//                    errorModel.setCode(JSONException);
//                    errorModel.setMsg(JSONException_MSG);
//
//                    target.onFailure(errorModel);
//                }
//
//
//            }
//
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                ErrorModel model = new ErrorModel();
//                model.setMsg(t.getMessage().toString());
//                target.onFailure(model);
//            }
//        });
    }

    public void post(String url, Map<String, String> parameters, Subscriber<String> subscriber) {
        apiService.executePost(url, parameters);

    }

//    public void upload(String url, RequestBody requestBody, Subscriber<ResponseBody> subscriber) {
//        apiService.upLoadFile(url, requestBody);
//
//    }

    public void download(String url, final CallBack callBack) {
        apiService.downloadFile(url);

    }

    Observable.Transformer schedulersTransformer() {
        return new Observable.Transformer() {


            @Override
            public Object call(Object observable) {
                return ((Observable) observable).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }

           /* @Override
            public Observable call(Observable observable) {
                return observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }*/
        };
    }


    /**
     * ApiBaseUrl
     *
     * @param newApiBaseUrl
     */
    public static void changeApiBaseUrl(String newApiBaseUrl) {
        baseUrl = newApiBaseUrl;
        builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl);
    }

    /**
     * addcookieJar
     */
    public static void addCookie() {
        okHttpClient.newBuilder().cookieJar(new NovateCookieManger(mContext)).build();
        retrofit = builder.client(okHttpClient).build();
    }

    /**
     * ApiBaseUrl
     *
     * @param newApiHeaders
     */
    public static void changeApiHeader(Map<String, String> newApiHeaders) {

        okHttpClient.newBuilder().addInterceptor(new BaseInterceptor(newApiHeaders)).build();
        builder.client(httpClient.build()).build();
    }

    /**
     * create BaseApi  defalte ApiManager
     *
     * @return ApiManager
     */
    public RetrofitClient createBaseApi() {
        apiService = create(BaseApiService.class);
        return this;
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulersTransformer();
    }

    public <T> Observable.Transformer<BaseResponse<T>, T> transformer() {

        return new Observable.Transformer() {

            @Override
            public Object call(Object observable) {
                return ((Observable) observable).map(new HandleFuc<T>()).onErrorResumeNext(new HttpResponseFunc<T>());
            }
        };
    }

    public <T> Observable<T> switchSchedulers(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static class HttpResponseFunc<T> implements Func1<Throwable, Observable<T>> {
        @Override
        public Observable<T> call(Throwable t) {
            return Observable.error(ExceptionHandle.handleException(t));
        }
    }

    private class HandleFuc<T> implements Func1<BaseResponse<T>, T> {
        @Override
        public T call(BaseResponse<T> response) {
            if (!response.isOk())
                throw new RuntimeException(response.getCode() + "" + response.getMsg() != null ? response.getMsg() : "");
            Log.i("====", response.getData().toString());
            return response.getData();
        }
    }


    /**
     * /**
     * execute your customer API
     * For example:
     * MyApiService service =
     * RetrofitClient.getInstance(MainActivity.this).create(MyApiService.class);
     * <p>
     * RetrofitClient.getInstance(MainActivity.this)
     * .execute(service.lgon("name", "password"), subscriber)
     * * @param subscriber
     */

    public static <T> T execute(Observable<T> observable, Subscriber<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        return null;
    }


    /**
     * DownSubscriber
     *
     * @param <ResponseBody>
     */
    class DownSubscriber<ResponseBody> extends Subscriber<ResponseBody> {
        CallBack callBack;

        public DownSubscriber(CallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        public void onStart() {
            super.onStart();
            if (callBack != null) {
                callBack.onStart();
            }
        }

        @Override
        public void onCompleted() {
            if (callBack != null) {
                callBack.onCompleted();
            }
        }

        @Override
        public void onError(Throwable e) {
            if (callBack != null) {
                callBack.onError(e);
            }
        }

        @Override
        public void onNext(ResponseBody responseBody) {
            DownLoadManager.getInstance(callBack).writeResponseBodyToDisk(mContext, (okhttp3.ResponseBody) responseBody);

        }
    }

}
