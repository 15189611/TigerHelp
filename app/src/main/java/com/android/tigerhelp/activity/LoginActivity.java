package com.android.tigerhelp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tigerhelp.R;
import com.android.tigerhelp.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by huangTing on 2016/12/14.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.register_login)
    TextView register_login;

    @Bind(R.id.username)
    EditText username;
    @Bind(R.id.password)
    EditText password;

    @Bind(R.id.self_login)
    FrameLayout self_login;
    @Bind(R.id.login_zid_iv)
    ImageView login_zid_iv;

    @Bind(R.id.login_bt)
    Button login_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.login;
    }

    @Override
    protected void initView() {

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

    @OnClick(R.id.register_login)
    public void toRegisterOnClick(){
        startActivity(new Intent(this,RegisterActivity.class));
    }


    @OnClick(R.id.login_bt)
    public void loginOnClick(){
        String mobileNum = username.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();

        if(TextUtils.isEmpty(mobileNum)){
//            AppToast.showToast(this,"手机号不能为空");
            return;
        }

        if(TextUtils.isEmpty(passwordStr)){
//            AppToast.showToast(this,"密码不能为空");
            return;
        }

//        loginRequest("13788888888","670b14728ad9902aecba32e22fa4f6bd");

    }


   /* private void loginRequest(String mobile,String password){
        LoginRequest.newInstance().login(this, mobile, password, Method.LOGIN.toString(), new ResponseListener<LoginModel>() {
            @Override
            public void onSuccess(LoginModel loginModel) {
                Log.i("TAG","login------onSuccess");
            }

            @Override
            public void onFailure(AppException e) {
                Log.i("TAG","login------onFailure"+e.errorMsg);
            }
        });
    }*/

}
