package com.android.tigerhelp.request;

import android.app.Activity;

import com.android.tigerhelp.entity.FileUploadModel;
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

    /***
     * 用户登录
     * @param activity
     * @param mobile
     * @param password
     * @param requestMethod
     * @param listener
     */
    public void login(Activity activity,String mobile,String password, String requestMethod, ResponseListener<UserModel> listener){
        Map<String, Object> map = new HashMap<>();
        map.put("mobileNum", mobile);
        map.put("password", password);

        Type type = new TypeToken<UserModel>() {
        }.getType();
        requestDataWithDialog(activity,requestMethod,map,type,listener);

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
        Map<String, Object> map = new HashMap<>();
        map.put("mobileNum", mobileNum);
        map.put("type", type);//type:1-注册获取验证码（一键登录页调用此），2-忘记密码获取验证码, 3-设置支付密码获取验证码

        Type typeModel = new TypeToken<String>() {
        }.getType();
        requestDataWithDialog(activity,requestMethod,map,typeModel,listener);
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
        Map<String,Object> map = new HashMap<>();
        map.put("mobileNum",mobileNum);
        map.put("code",code);

        Type type = new TypeToken<UserModel>() {
        }.getType();

        requestDataWithDialog(activity,requestMethod,map,type,listener);
    }

}
