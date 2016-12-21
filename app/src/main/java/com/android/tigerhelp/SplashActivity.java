package com.android.tigerhelp;

import android.content.Intent;

import com.android.tigerhelp.activity.MainActivity;
import com.android.tigerhelp.base.BaseActivity;
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
