package com.android.tigerhelp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tigerhelp.R;
import com.android.tigerhelp.base.BaseActivity;
import com.android.tigerhelp.entity.UserModel;
import com.android.tigerhelp.http.AppException;
import com.android.tigerhelp.http.responselistener.ResponseListener;
import com.android.tigerhelp.request.UserRequest;
import com.android.tigerhelp.util.PhoneUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huangTing on 2016/12/19.
 */
public class RegisterActivity extends BaseActivity {

    @Bind(R.id.username_register)
    EditText username_register;
    @Bind(R.id.code_register)
    EditText code_register;

    @Bind(R.id.register_login)
    TextView register_login;
    @Bind(R.id.getCode)
    ImageView getCode;

    @Bind(R.id.register_bt)
    Button register_bt;


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
    public void toLoginOnClick() {
        startActivity(new Intent(this, LoginActivity.class));
        this.finish();
    }

    @OnClick(R.id.getCode)
    public void getCode(){
        String mobile = username_register.getText().toString().trim();
        String code = code_register.getText().toString().trim();

        if(TextUtils.isEmpty(mobile)){
            showToast("请输入手机号码");
            return;
        }

        if(!PhoneUtil.isMobileNO(mobile)){/**校验手机号是否规范*/
            showToast("请输入正确的11位手机号号码");
            return;
        }

        /***
         * type:1-注册获取验证码（一键登录页调用此），2-忘记密码获取验证码, 3-设置支付密码获取验证码
         */
        getRegisterCode(mobile,"1");
    }

    @OnClick(R.id.register_bt)
    public void registerBt(){
        String mobile = username_register.getText().toString().trim();
        String code = code_register.getText().toString().trim();

        if(TextUtils.isEmpty(mobile)){
            showToast("请输入手机号码");
            return;
        }

        if(!PhoneUtil.isMobileNO(mobile)){/**校验手机号是否规范*/
            showToast("请输入正确的11位手机号号码");
            return;
        }

        if(TextUtils.isEmpty(code)){
            showToast("请输入验证码");
           return;
        }
        /***
         * 测试阶段code=000000
         * 目前没有短信获取包
         */
        code = "000000";
        registerRequest(mobile,code);
    }

    public void getRegisterCode(String mobileNum,String type){
        UserRequest.newInstance().getCode(this, mobileNum, type, "appUser/getCode", new ResponseListener<String>() {
            @Override
            public void onSuccess(String state) {
            }

            @Override
            public void onFailure(AppException e) {
                if(TextUtils.isEmpty(e.errorCode)){
                    if(!TextUtils.isEmpty(e.errorMsg)){
                        showToast(e.errorMsg);
                    }else{
                        showToast("验证码获取失败");
                    }
                    return;
                }
                if(e.errorCode.equals("0")){/**获取成功，则都是失败*/
                    showToast("验证码获取成功");
                }else{
                    showToast("验证码获取失败");
                }
            }
        });
    }

    public void registerRequest(String mobileNum,String code){
        UserRequest.newInstance().register(this, mobileNum, code, "appUser/register", new ResponseListener<UserModel>() {
            @Override
            public void onSuccess(UserModel userModel) {
                String token = userModel.getToken();
                int userid = userModel.getUserid();
                boolean turnUpdateUser = userModel.isTurnUpdateUser();
                showToast("用户注册成功");
                /**是否跳转到更新用户资料页面,true是，false:直接进首页*/
                if(turnUpdateUser){
                    startActivity(new Intent(RegisterActivity.this,PersonDataActivity.class));
                    RegisterActivity.this.finish();
                }else{
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    RegisterActivity.this.finish();
                }
            }

            @Override
            public void onFailure(AppException e) {
                if(!TextUtils.isEmpty(e.errorMsg)){
                    showToast(e.errorMsg);
                }else{
                    showToast("用户注册失败");
                }
            }
        });
    }
}
