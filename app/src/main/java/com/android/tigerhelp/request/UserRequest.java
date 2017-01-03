package com.android.tigerhelp.request;

import android.app.Activity;

import com.android.tigerhelp.entity.UserModel;
import com.android.tigerhelp.http.TigerRequest;
import com.android.tigerhelp.http.request.RequestParamBuilder;
import com.android.tigerhelp.http.responselistener.ResponseListener;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


/**
 * Created by Charles on 2016/12/23.
 */

public class UserRequest extends TigerRequest {
    public static UserRequest newInstance() {
        return new UserRequest();
    }

    /***
     * 用户登录
     * @param activity
     * @param mobile
     * @param password
     * @param requestMethod
     * @param listener
     */
    public void login(Activity activity,String mobile,String password, String requestMethod, ResponseListener<UserModel> listener){
        RequestParamBuilder requestParamBuilder = RequestParamBuilder.newInstance();

        requestParamBuilder.putParam("mobileNum", mobile);
        requestParamBuilder.putParam("password", password);

        Type type = new TypeToken<UserModel>() {
        }.getType();
        requestDataWithDialog(activity,requestMethod,requestParamBuilder.create(),type,listener);

    }

    /***
     * 用户注册获取验证码
     * @param activity
     * @param mobileNum
     * @param type
     * @param requestMethod
     * @param listener
     */
    public void getCode(Activity activity, String mobileNum, String type, String requestMethod, ResponseListener<String> listener){
        RequestParamBuilder requestParamBuilder = RequestParamBuilder.newInstance();
        requestParamBuilder.putParam("mobileNum", mobileNum);
        requestParamBuilder.putParam("type", type);//type:1-注册获取验证码（一键登录页调用此），2-忘记密码获取验证码, 3-设置支付密码获取验证码

        Type typeModel = new TypeToken<String>() {
        }.getType();
        requestDataWithDialog(activity,requestMethod,requestParamBuilder.create(),typeModel,listener);
    }

    /****
     * 用户注册
     * @param activity
     * @param mobileNum
     * @param code
     * @param requestMethod
     * @param listener
     */
    public void register(Activity activity,String mobileNum,String code,String requestMethod,ResponseListener<UserModel> listener){
        RequestParamBuilder requestParamBuilder = RequestParamBuilder.newInstance();
        requestParamBuilder.putParam("mobileNum",mobileNum);
        requestParamBuilder.putParam("code",code);

        Type type = new TypeToken<UserModel>() {
        }.getType();

        requestDataWithDialog(activity,requestMethod,requestParamBuilder.create(),type,listener);
    }

}
