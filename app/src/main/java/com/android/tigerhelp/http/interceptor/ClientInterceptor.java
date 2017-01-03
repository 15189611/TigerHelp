package com.android.tigerhelp.http.interceptor;


import com.android.tigerhelp.BuildConfig;
import com.android.tigerhelp.util.BussinessUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class ClientInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request copy =  chain.request().newBuilder().build();
        Buffer buffer = new Buffer();
        copy.body().writeTo(buffer);
        String header = buffer.readUtf8();
        String mTimestamp = null;
        try {
            JSONObject jsonObject = new JSONObject(header);
            mTimestamp = jsonObject.getString("timestamp");
        } catch (JSONException e) {
            mTimestamp = String.valueOf(System.currentTimeMillis());
            e.printStackTrace();
        }
        if(!BussinessUtil.isValid(mTimestamp)){
            mTimestamp = String.valueOf(System.currentTimeMillis());
        }

        final Request request = chain.request().newBuilder()
                .addHeader("Connection", "Keep-Alive")
                .addHeader("Charset", "UTF-8")
                .addHeader("timestamp",mTimestamp)
                .build();
        if (BuildConfig.DEBUG) {
            Response response = chain.proceed(request);
            return interceptLogWithResponse(response, request);
        }

        return chain.proceed(request);
    }

    private Response interceptLogWithResponse(Response response, Request request) {
        try {
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            ResponseBody body = clone.body();
            if (body == null) {
                return response;
            }

            MediaType mediaType = body.contentType();
            if (mediaType == null) {
                return response;

            }

            String resp = body.string();
            printRequestUrl(request);
           // Trace.json(resp);
            body = ResponseBody.create(mediaType, resp);
            return response.newBuilder().body(body).build();
        } catch (Exception ignored) {
        }

        return response;
    }

    private void printRequestUrl(Request request) {
        if (request.body() instanceof FormBody) {
            FormBody formBody = (FormBody) request.body();
            if (formBody.size() < 3) {
                return;
            }
            String requestMethod = formBody.encodedValue(2);
        }
    }

}
