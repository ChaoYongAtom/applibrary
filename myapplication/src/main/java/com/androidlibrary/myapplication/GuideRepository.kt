package com.androidlibrary.myapplication

import com.alibaba.fastjson.JSONObject
import com.ruiyun.comm.library.live.BaseRepository
import com.ruiyun.comm.library.live.interfaces.CallBack
import com.wcy.app.lib.update.VersionBean

/**
 *GuideRepository
 *@auth wangchaoyong
 *@time 2019/1/20
 *@description  YjSales
 *@version 4.0.0
 */
class GuideRepository : BaseRepository() {
    fun init(callBack: CallBack) {
        sendPost("newestversion", null, VersionBean::class.java, callBack)
    }

    fun login(callBack: CallBack) {
        val parameters = JSONObject()
        parameters["operatorAccount"] = "18680758532"
        parameters["operatorPwd"] = "1234567"
        sendPost("platform/login", parameters,callBack)
    }

}