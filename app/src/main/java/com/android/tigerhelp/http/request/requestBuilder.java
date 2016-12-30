package com.android.tigerhelp.http.request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Charles on 2016/10/17.
 */

public class requestBuilder {

    Map<String, Object> methodParams = new HashMap<>();

    public static requestBuilder newInstance() {
        return new requestBuilder();
    }

    public requestBuilder(){
        init();
    }

    private void init() {

    }

}
