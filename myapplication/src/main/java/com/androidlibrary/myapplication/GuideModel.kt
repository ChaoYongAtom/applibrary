package com.androidlibrary.myapplication

import android.app.Application
import com.ruiyun.comm.library.live.BaseViewModel

/**
 *GuideViewModel
 *@auth wangchaoyong
 *@time 2019/1/20
 *@description  YjSales
 *@version 4.0.0
 */
class GuideModel(application: Application) : BaseViewModel<GuideRepository>(application) {

    fun loading() {
        mRepository.init(this)
    }

    fun login() {
        mRepository.login(this)
    }

}