package com.android.tigerhelp.http.request;

import com.android.tigerhelp.TigerApplication;
import com.android.tigerhelp.db.ShareUtils;
import com.android.tigerhelp.util.BussinessUtil;
import com.android.tigerhelp.util.MD5;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Charles on 2016/10/17.
 */

public class RequestParamBuilder {

    Map<String, Object> methodParams = new HashMap<>();

    public static RequestParamBuilder newInstance() {
        return new RequestParamBuilder();
    }

    public RequestParamBuilder(){
        init();
    }

    private void init() {
        String userId = (String) ShareUtils.get(TigerApplication.getMyApplicationContext(), "userId");
        String token = (String) ShareUtils.get(TigerApplication.getMyApplicationContext(), "token");
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        if(BussinessUtil.isValid(userId) && BussinessUtil.isValid(token)){
            String sign = MD5.getMD5Str(currentTimeMillis + userId + token + userId);
            methodParams.put("token",token);
            methodParams.put("sign",sign);
        }
        methodParams.put("timestamp",currentTimeMillis);
    }

    public void putParam(String key, Object value){
        methodParams.put(key, value);
    }

    public Map<String, Object> create(){
        return methodParams;
    }

}
