package com.androidlibrary.myapplication

import com.ruiyun.comm.library.mvvm.BaseRepository
import com.ruiyun.comm.library.mvvm.interfaces.CallBack
/**
 *GuideRepository
 *@auth wangchaoyong
 *@time 2019/1/20
 *@description  YjSales
 *@version 4.0.0
 */
class GuideRepository : BaseRepository() {
    fun init(callBack: CallBack) {
        sendPost("newestversion", null,null, false, callBack)
    }
}