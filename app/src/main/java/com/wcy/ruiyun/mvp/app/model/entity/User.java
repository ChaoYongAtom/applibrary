package com.wcy.ruiyun.mvp.app.model.entity;

import java.io.Serializable;

/**
 * Created by admin on 2018/2/23.
 */

public class User implements Serializable{
    public String name;
    public Integer id;

    public User(String name, Integer id) {
        this.name = name;
        this.id = id;
    }
}
