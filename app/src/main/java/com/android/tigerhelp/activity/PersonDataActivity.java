package com.android.tigerhelp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.tigerhelp.R;
import com.android.tigerhelp.base.BaseActivity;

/**
 * Created by huangTing on 2016/12/23.
 */

public class PersonDataActivity  extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.personal_data;
    }

    @Override
    protected void initView() {
        setTitle("个人资料");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getRemoteData() {

    }
}
