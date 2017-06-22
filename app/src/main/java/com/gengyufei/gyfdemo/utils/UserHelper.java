package com.gengyufei.gyfdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.gengyufei.gyfdemo.entity.User;

/**
 * Created by FineFan on 2017/6/13.
 */

public class UserHelper {
    private User mUser;

    private final static String SP_NAME = "user_sp";

    private volatile static UserHelper instance;

    private UserHelper() {
    }

    public static UserHelper getInstance() {
        if (instance == null) {
            synchronized (UserHelper.class) {
                if (instance == null) {
                    instance = new UserHelper();
                    instance.mUser = new User();
                }
            }
        }
        return instance;
    }

    public boolean isLoggedIn() {

        return getUser() != null && getUserId() > 0 && !TextUtils.isEmpty(getToken());
    }

    public void init(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE);
        mUser.setToken(sp.getString("token", ""));
        mUser.setNickName(sp.getString("nickName", ""));
        mUser.setId(sp.getInt("id", 0));
    }

    public void save(Context context, User user) {
        if (user == null) return;
        mUser = user;
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE);
        sp.edit()
                .putString("token", getToken())
                .putString("nickName", getNickName())
                .putInt("id", getUserId())
                .apply();
    }


    public void clear() {
        mUser.setToken("");
        mUser.setName("");
        mUser.setNickName("");
        mUser.setId(0);
    }

    public User getUser() {
        return mUser;
    }


    public int getUserId() {
        return getUser().getId();
    }

    public String getToken() {
        return getUser().getToken();
    }

    public String getName() {
        return getUser().getName();
    }

    public String getNickName() {
        return getUser().getNickName();
    }


}
