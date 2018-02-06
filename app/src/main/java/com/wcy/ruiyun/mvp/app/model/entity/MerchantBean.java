package com.wcy.ruiyun.mvp.app.model.entity;

import java.io.Serializable;

/**
 * Created by wcy on 2017/5/23.
 */

public class MerchantBean implements Serializable {


    /**
     * token : 78c7dea91e24462fe784f9e5bece2462
     * isDeffBuildingProjectId : 0
     * isChangeMobile : 0
     */

    public String token = "";//
    public boolean isDeffBuildingProjectId = false;//是否设置了默认楼盘
    public String nikeName = "";//	用户昵称
    public String account = "";//登录帐号
    public String operatorRole;//用户当前角色
    public int intelligent;//是否显示智能分组//0关闭、1开启(智能分组)
    private int roleType;//1置业顾问 2销售经理

    public int getRoleType() {
        if (operatorRole.equals("置业顾问")) {
            roleType = 1;
        } else {
            roleType = 2;
        }
        return roleType;
    }
}
