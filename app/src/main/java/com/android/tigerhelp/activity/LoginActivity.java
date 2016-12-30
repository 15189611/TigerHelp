package com.android.tigerhelp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tigerhelp.R;
import com.android.tigerhelp.base.BaseActivity;
import com.android.tigerhelp.db.ShareUtils;
import com.android.tigerhelp.entity.UserModel;
import com.android.tigerhelp.http.AppException;
import com.android.tigerhelp.http.responselistener.ResponseListener;
import com.android.tigerhelp.request.UserRequest;
import com.android.tigerhelp.util.MD5;
import com.android.tigerhelp.util.PhoneUtil;

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
            showToast("手机号不能为空");
            return;
        }

        if(!PhoneUtil.isMobileNO(mobileNum)){/**校验手机号是否规范*/
            showToast("请输入正确的11位手机号号码");
            return;
        }

        if(TextUtils.isEmpty(passwordStr)){
            showToast("密码不能为空");
            return;
        }

        String passwordMd5 = MD5.getMD5Str("000000");
        Log.i("TAG","pass----md5======"+passwordMd5);
        mobileNum = "13788888888";/**测试号码*/
        loginRequest(mobileNum,passwordMd5);
    }

   private void loginRequest(String mobile,String password){
       UserRequest.newInstance().login(this, mobile,password,"appUser/login", new ResponseListener<UserModel>() {
           @Override
           public void onSuccess(UserModel userModel) {
               showToast("登录成功");
               int userid = userModel.getUserid();
               String token = userModel.getToken();
               ShareUtils.save(LoginActivity.this,"userId",userid);
               ShareUtils.save(LoginActivity.this,"token",token);

               boolean turnUpdateUser = userModel.isTurnUpdateUser();
               /**是否跳转到更新用户资料页面,true是，false:直接进首页*/
               if(turnUpdateUser){
                  startActivity(new Intent(LoginActivity.this,PersonDataActivity.class));
                   LoginActivity.this.finish();
               }else{
                   startActivity(new Intent(LoginActivity.this,MainActivity.class));
                   LoginActivity.this.finish();
               }
           }

           @Override
           public void onFailure(AppException e) {
               if(!TextUtils.isEmpty(e.errorMsg)){
                   showToast(e.errorMsg);
               }else{
                   showToast("登录失败");
               }
           }
       });
    }

}
