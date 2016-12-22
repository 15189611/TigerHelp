package com.android.tigerhelp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.tigerhelp.R;
import com.android.tigerhelp.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huangTing on 2016/12/19.
 */
public class RegisterActivity extends BaseActivity {

    @Bind(R.id.register_login)
    TextView register_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.register;
    }

    @Override
    protected void initView() {
//        setTitle("注册");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getRemoteData() {

    }

    @Override
    protected boolean enableToolbar() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.register_login)
    public void toLoginOnClick(){
        startActivity(new Intent(this,LoginActivity.class));
        this.finish();
    }

}
