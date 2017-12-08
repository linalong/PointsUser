package com.heizi.mylibrary.retrofit2;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Ｔａｍｉｃ on 2016-07-08.
 * {@link # https://github.com/NeglectedByBoss/RetrofitClient}
 */
public interface BaseApiService {


    @GET(" ")
    Call<String> executeGet(
            @QueryMap Map<String, String> maps
    );

    @FormUrlEncoded
    @POST("{url}")
    Call<String> executePost(
            @Path("url") String url,
            //  @Header("") String authorization,
            @FieldMap Map<String, String> maps);

    //    @FormUrlEncoded
    @Multipart
    @POST("/index.php")
    Call<String> upLoadFile(
            @Part("image\"; filename=\"image.jpg") RequestBody requestBody, @Query("token") String token, @Query("r") String
            r);

    @Multipart
    @POST("{url}")
    Call<String> upLoadFile(
            @Part("image\"; filename=\"image.jpg") RequestBody requestBody);

    @FormUrlEncoded
    @POST("/index.php")
    Call<String> upLoadFile(
            @Part("image\"; filename=\"image.jpg") RequestBody requestBody, @FieldMap Map<String, String> maps);

    @Multipart
    @POST(" ")
    Call<String> upLoadFiles(
            @PartMap() Map<String, RequestBody> maps);

    @Streaming
    @GET
    Call<String> downloadFile(@Url String fileUrl);

}
