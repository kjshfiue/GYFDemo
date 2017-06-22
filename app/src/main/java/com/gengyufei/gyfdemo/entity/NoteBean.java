package com.gengyufei.gyfdemo.entity;

import java.io.Serializable;

/**
 * Created by FineFan on 2017/6/12.
 */

public class NoteBean implements Serializable{
    public int id;
    public int userId;

    public String content;
    public long updateTime;
    public long createTime;

    public String type;

    public NoteBean(){}


}
