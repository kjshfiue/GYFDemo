package com.gengyufei.gyfdemo.entity;

import java.util.Random;

/**
 * Created by FineFan on 2017/6/12.
 */

public class User {
    private int id;
    private String token;
    private String name;
    private String password;
    private String nickName;

    public User() {
    }




    public String createRandomToken() { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789QWERTYUIOPLKJHGFDSAZXCVBNM";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 24; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        String val = String.valueOf(System.currentTimeMillis());
        sb.append("_").append(val.substring(val.length() - 10, val.length() ));
        return sb.toString();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
