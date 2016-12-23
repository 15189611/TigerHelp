package com.android.tigerhelp.request;

import android.app.Activity;

import com.android.tigerhelp.entity.UserModel;
import com.android.tigerhelp.http.TigerRequest;
import com.android.tigerhelp.http.responselistener.ResponseListener;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Charles on 2016/12/23.
 */

public class UserRequest extends TigerRequest {
    public static UserRequest newInstance() {
        return new UserRequest();
    }

    public void login(Activity activity, String requestMethod, ResponseListener<UserModel> listener){
        Map<String, Object> map = new HashMap<>();
        map.put("mobileNum", "13788888888");
        map.put("password", "670b14728ad9902aecba32e22fa4f6bd");

        Type type = new TypeToken<UserModel>() {
        }.getType();
        requestPostWithActivity(activity,requestMethod,map,type,listener);

    }

}
