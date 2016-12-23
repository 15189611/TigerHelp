package com.android.tigerhelp.http.subscribers;


import com.android.tigerhelp.http.responselistener.ResponseListener;

public class ResponseSubscriber<T> extends BaseSubscriber<T> {

    public ResponseSubscriber(ResponseListener<T> responseListener) {
        super(responseListener);
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
    }

    @Override
    public void onNext(T t) {
        super.onNext(t);
    }
}