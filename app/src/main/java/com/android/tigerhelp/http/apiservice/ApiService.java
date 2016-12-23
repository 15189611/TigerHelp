package com.android.tigerhelp.http.apiservice;


import com.android.tigerhelp.http.ResponseResult;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Charles
 */
public interface ApiService {
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST()
    Observable<ResponseResult> getPostData(@Url String url, @Body RequestBody  body);

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @GET()
    Observable<ResponseResult> getWithGetData(@Url String url, @Body RequestBody  body);

}
