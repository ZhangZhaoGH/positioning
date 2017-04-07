package com.example.administrator.positioning.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.administrator.positioning.R;
import com.example.administrator.positioning.api.ApiConstants;
import com.example.administrator.positioning.base.BaseActivity;
import com.example.administrator.positioning.utils.SPUtils;
import com.example.administrator.positioning.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;


public class MainActivity extends BaseActivity {
    private EditText etMainLogin;
    private EditText etMainPassword;
    private Button btnMainLogin;
    private LinearLayout llMainRememberAccount;
    private ImageView ivMainRememberIcon;
    private TextView title;

    private String account, password;
    private boolean isRememberAccount;
    private Toolbar mToolbar;

    @Override
    public void doBusiness(Context mContext) {
        String account= (String) SPUtils.get(this,SPUtils.SPU_ACCOUNT,"");
        String password= (String) SPUtils.get(this,SPUtils.SPU_PASSWORD,"");
        if (!TextUtils.isEmpty(account)){
            isRememberAccount=true;
            ivMainRememberIcon.setImageResource(R.mipmap.ic_launcher);
            etMainLogin.setText(account);
            etMainPassword.setText(password);
        }
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void setPresenter() {

    }

    @Override
    public void initView(View view) {
        mToolbar= (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        title= (TextView) view.findViewById(R.id.tv_toolbar_title);
        title.setText("登录");
        etMainLogin= (EditText) view.findViewById(R.id.et_main_login);
        etMainPassword= (EditText) view.findViewById(R.id.et_main_password);
        btnMainLogin= (Button) view.findViewById(R.id.btn_main_login);
        llMainRememberAccount= (LinearLayout) view.findViewById(R.id.ll_main_remember_account);
        ivMainRememberIcon= (ImageView) view.findViewById(R.id.iv_main_remember_icon);
        btnMainLogin.setOnClickListener(this);
        llMainRememberAccount.setOnClickListener(this);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_login:
                account = etMainLogin.getText().toString();
                password = etMainPassword.getText().toString();

                if (TextUtils.isEmpty(account)) {
                    ToastUtils.showToast("请输入账号");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.showToast("请输入密码");
                    return;
                }

                login();
                break;
            case R.id.ll_main_remember_account:
                isRememberAccount = !isRememberAccount;
                if (isRememberAccount) {
                    SPUtils.put(this, SPUtils.SPU_ACCOUNT, account);
                    SPUtils.put(this, SPUtils.SPU_PASSWORD, password);
                    ivMainRememberIcon.setImageResource(R.mipmap.ic_launcher);
                }else {
                    SPUtils.remove(this,SPUtils.SPU_ACCOUNT);
                    SPUtils.remove(this, SPUtils.SPU_PASSWORD);
                    ivMainRememberIcon.setImageResource(R.mipmap.ic_launcher);
                }
                break;
            default:
                break;
        }
    }

    private void login() {
        String url= ApiConstants.getLoginUrl(account,password);
        OkHttpUtils.post().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showToast("获取登录数据失败，请重试！");
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject= JSON.parseObject(response);
                    JSONObject head=jsonObject.getJSONObject("head");
                    String issuccess=head.getString("issuccess");
                    if ("false".equals(issuccess)){
                        ToastUtils.showToast("登录失败，请检查账号或密码");
                        return;
                    }

                    JSONObject body=jsonObject.getJSONObject("body");
                    String result=body.getString("result");
                    ToastUtils.showToast(result);
                    startActivity(ClassCarInfoActivity.class);

                }catch (Exception e){
                    ToastUtils.showToast("获取登录数据失败，请重试！");
                    e.printStackTrace();
                }
            }
        });

    }

}
