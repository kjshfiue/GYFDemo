package com.gengyufei.gyfdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.gengyufei.gyfdemo.BaseActivity;
import com.gengyufei.gyfdemo.MainActivity;
import com.gengyufei.gyfdemo.R;
import com.gengyufei.gyfdemo.db.DBManager;
import com.gengyufei.gyfdemo.entity.User;
import com.gengyufei.gyfdemo.utils.UserHelper;


public class LoginActivity extends BaseActivity {
    private EditText et_name, et_pwd, et_nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_name = (EditText) findViewById(R.id.et_name);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_nick = (EditText) findViewById(R.id.et_nick);


        //登录
        findViewById(R.id.tv_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString().trim();
                String pwd = et_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
                    toast("账号和密码不能为空");
                    return;
                }

                if (pwd.length() < 6) {
                    toast("密码不能少于6位");
                    return;
                }
                login(name, pwd);
            }
        });


        //注册
        findViewById(R.id.tv_reg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString().trim();
                String pwd = et_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
                    toast("账号和密码不能为空");
                    return;
                }
                if (pwd.length() < 6) {
                    toast("密码不能少于6位");
                    return;
                }
                reg(name, pwd);
            }
        });

    }

    private void login(String name, String pwd) {
        User user = DBManager.getInstance(mActivity).loadUser(name, pwd);
        if (user != null && user.getId() > 0) {
            toast("登录成功");
            UserHelper.getInstance().save(mActivity, user);
            startActivity(new Intent(mActivity, MainActivity.class));
            finish();
        } else {
            toast("账号密码错误或账号不存在");
        }


    }

    private void reg(String name, String pwd) {
        User user = DBManager.getInstance(mActivity).loadUser(name, pwd);
        if (user == null || user.getId() < 1) {
            user = new User();
            user.setNickName(et_nick.getText().toString().trim());
            user.setName(name);
            user.setPassword(pwd);
            user.setToken(user.createRandomToken());
            user.setId(DBManager.getInstance(mActivity).insertUser(user));
            if (user.getId() > 0) {
                toast("注册成功");
                UserHelper.getInstance().save(mActivity, user);
                startActivity(new Intent(mActivity, MainActivity.class));
                finish();
            } else {
                toast("注册失败");
            }
        } else {
            toast("账号已存在");
        }


    }

}
