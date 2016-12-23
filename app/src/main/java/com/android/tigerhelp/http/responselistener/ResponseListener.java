package com.android.tigerhelp.http.responselistener;


import com.android.tigerhelp.http.AppException;

public interface ResponseListener<T> {

    void onSuccess(T t);

    void onFailure(AppException e);

}
