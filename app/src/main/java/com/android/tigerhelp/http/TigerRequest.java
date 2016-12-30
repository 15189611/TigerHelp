package com.android.tigerhelp.http;

import android.app.Activity;
import android.util.Log;

import com.android.tigerhelp.http.apiservice.ApiService;
import com.android.tigerhelp.http.responselistener.ResponseListener;
import com.android.tigerhelp.http.subscribers.BaseSubscriber;
import com.android.tigerhelp.http.subscribers.ProgressResponseSubscriber;
import com.android.tigerhelp.util.BussinessUtil;
import com.android.tigerhelp.util.JsonParser;
import com.android.tigerhelp.util.JsonUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Charles on 2016/12/23.
 */

public class TigerRequest {

    private static final ConcurrentHashMap<Object, Map<String, Subscriber<?>>> mSubscriberMap = new ConcurrentHashMap<>();

    public TigerRequest() {
    }

    public static void removeRequest(Object object, String requestMethod) {
        synchronized (mSubscriberMap) {
            if (object != null && mSubscriberMap.containsKey(object)) {
                Map<String, Subscriber<?>> subscriberMap = mSubscriberMap.get(object);
                if (subscriberMap != null && subscriberMap.containsKey(requestMethod)) {
                    subscriberMap.remove(requestMethod);
                }
            }
        }
    }

    private static void cancelRequest(Object object, String requestMethod) {
        synchronized (mSubscriberMap) {
            if (mSubscriberMap.containsKey(object)) {
                Map<String, Subscriber<?>> subscriberMap = mSubscriberMap.get(object);
                if (subscriberMap != null && subscriberMap.containsKey(requestMethod)) {
                    Subscriber<?> subscriber = subscriberMap.get(requestMethod);
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.unsubscribe();
                    }
                    subscriberMap.remove(requestMethod);
                }
            }
        }
    }

    public static void cancelAll(Object object) {
        synchronized (mSubscriberMap) {
            if (mSubscriberMap.containsKey(object)) {
                Map<String, Subscriber<?>> subscriberMap = mSubscriberMap.get(object);
                if (subscriberMap != null) {
                    Iterator<Map.Entry<String, Subscriber<?>>> iterator = subscriberMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, Subscriber<?>> entry = iterator.next();
                        Subscriber<?> subscriber = entry.getValue();
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.unsubscribe();
                        }
                        iterator.remove();
                    }
                }

                mSubscriberMap.remove(object);
            }
        }
    }

    /**
     * Post 提交
     */
    @SuppressWarnings("unchecked")
    public <T> void requestPostWithActivity(Activity activity , String requestMethod, Map<String, Object> params, Type type , ResponseListener<T> listener){
        if (activity != null && BussinessUtil.isValid(requestMethod)) {
            cancelRequest(activity, requestMethod);
        }
        Subscriber<T> subscriber = new BaseSubscriber<>(activity,requestMethod,listener);

        toSubscribe(requestMethod,params,type,subscriber);

        addSubscribe(activity, requestMethod, subscriber);
    }

    /**
     * Get 提交
     */
    @SuppressWarnings("unchecked")
    public <T> void requestGetWithActivity(Activity activity ,String requestMethod,Map<String, Object> params,Type type ,ResponseListener<T> listener){
        if (activity != null && BussinessUtil.isValid(requestMethod)) {
            cancelRequest(activity, requestMethod);
        }
        Subscriber<T> subscriber = new BaseSubscriber<>(activity,requestMethod,listener);

        toSubscribeGet(requestMethod,params,type,subscriber);

        addSubscribe(activity, requestMethod, subscriber);
    }

    /**
     * Post 提交 & 有Dialog
     */
    @SuppressWarnings("unchecked")
    public <T> void requestDataWithDialog(Activity activity, String requestMethod,Map<String, Object> params,Type type ,ResponseListener<T> listener) {
        if (activity == null || activity.isFinishing()) {
            toSubscribe(requestMethod, params, type, listener);
            return;
        }

        if (BussinessUtil.isValid(requestMethod)) {
            cancelRequest(activity, requestMethod);
        }

        Subscriber<T> subscriber = new ProgressResponseSubscriber<>(activity, requestMethod, listener);

        toSubscribe(requestMethod, params, type,subscriber);

        addSubscribe(activity, requestMethod, subscriber);

    }

    private <T> void toSubscribe(String requestMethod,Map<String, Object>  params, final Type type, final ResponseListener<T> listener) {
        Subscriber<T> subscriber = new BaseSubscriber<>(requestMethod, listener);

        toSubscribe(requestMethod,params, type, subscriber);
    }

    @SuppressWarnings("unchecked")
    private <T> void toSubscribe(String requestMethod,Map<String, Object> params, Type type, Subscriber<T> subscriber) {

        ApiService apiService = RetrofitManager.getInstance().getApiService();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JsonUtil.toJson(params));

        apiService
                .getPostData(requestMethod,body)
                .map(new ResponseResultFunc(type))
                .compose(requestScheduler())
                .subscribe(subscriber);
    }

    @SuppressWarnings("unchecked")
    private <T> void toSubscribeGet(String requestMethod,Map<String, Object> params, Type type, Subscriber<T> subscriber) {

        ApiService apiService = RetrofitManager.getInstance().getApiService();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JsonUtil.toJson(params));

        apiService
                .getWithGetData(requestMethod,body)
                .map(new ResponseResultFunc(type))
                .compose(requestScheduler())
                .subscribe(subscriber);
    }

    private <T> void addSubscribe(Activity activity, String requestMethod, Subscriber<T> subscriber) {
        if (activity != null && BussinessUtil.isValid(requestMethod)) {
            Map<String, Subscriber<?>> subscriberMap = mSubscriberMap.get(activity);
            if (subscriberMap == null) {
                subscriberMap = new HashMap<>();
                mSubscriberMap.put(activity, subscriberMap);
            }

            subscriberMap.put(requestMethod, subscriber);
        }
    }

    public class ResponseResultFunc<T> implements Func1<ResponseResult<T>, T> {
        private Type type;

        public ResponseResultFunc(Type type) {
            this.type = type;
        }

        @Override
        public T call(ResponseResult responseResult) {
            if(responseResult == null){
                throw new AppException(responseResult.state +"",responseResult.msg);
            }else{
                if (responseResult.data != null) {
                    return JsonParser.deserializeByJson(JsonParser.serializeToJson(responseResult.data), type); //将json字符串转成实体类
                }else{
                    throw new AppException(responseResult.state+"", responseResult.msg);
                }
            }

        }
    }

    public  <T> Observable.Transformer<T, T> requestScheduler() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public class ResponseFunc1<T> implements Func1<T, T> {
        @Override
        public T call(T responseResult) {
            if (responseResult == null) {
                throw new AppException(AppException.ExceptionStatus.ResultException, AppException.RESULT_ERROR);
            }
            return responseResult;
        }
    }

    public class ResponseFunc2<T> implements Func1<ResponseResult<T>, T> {
        @Override
        public T call(ResponseResult<T> responseResult) {
            return responseResult.data;
        }
    }

}
