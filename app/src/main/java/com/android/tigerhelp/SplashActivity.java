package com.android.tigerhelp;

import android.content.Intent;
import android.util.Log;

import com.android.tigerhelp.activity.MainActivity;
import com.android.tigerhelp.base.BaseActivity;
import com.android.tigerhelp.entity.UserModel;
import com.android.tigerhelp.http.AppException;
import com.android.tigerhelp.http.responselistener.ResponseListener;
import com.android.tigerhelp.request.UserRequest;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;


public class SplashActivity extends BaseActivity {

    private Subscription subscribe;

    @Override
    protected int getLayoutResId() {
        return R.layout.splash;
    }

    @Override
    protected void initView() { 
    }

    @Override
    protected void initData() {
        UserRequest.newInstance().login(this, "login", new ResponseListener<UserModel>() {
            @Override
            public void onSuccess(UserModel userModel) {
                Log.e("Charles2" , "成功");
            }

            @Override
            public void onFailure(AppException e) {
                Log.e("Charles2" , "失败");
            }
        });
    }

    @Override
    protected void getRemoteData() {
        delayJump();
    }

    private void delayJump() {
        subscribe = Observable.timer(3, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected boolean enableToolbar() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }

}
