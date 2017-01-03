package com.android.tigerhelp.request;

import android.app.Activity;

import com.android.tigerhelp.entity.HomePageModel;
import com.android.tigerhelp.http.TigerRequest;
import com.android.tigerhelp.http.request.RequestParamBuilder;
import com.android.tigerhelp.http.responselistener.ResponseListener;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by Charles on 2016/12/30.
 */

public class IndexRequest extends TigerRequest {
    public static final String INDEX_GET = "index/get";

    public static IndexRequest newInstance() {
        return new IndexRequest();
    }

    public void indexGetData(Activity activity, ResponseListener<HomePageModel> listener){
        String method = INDEX_GET;
        RequestParamBuilder requestParamBuilder = RequestParamBuilder.newInstance();

        Type type = new TypeToken<HomePageModel>() {
        }.getType();
        requestDataWithDialog(activity,method,requestParamBuilder.create(),type,listener);
    }


}
