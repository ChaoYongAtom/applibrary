package com.androidlibrary.myapplication

import android.app.Application
import com.ruiyun.comm.library.common.JConstant
import com.ruiyun.comm.library.mvvm.BaseViewModel
import com.ruiyun.comm.library.mvvm.RxResult
import org.wcy.android.retrofit.exception.ApiException

/**
 *GuideViewModel
 *@auth wangchaoyong
 *@time 2019/1/20
 *@description  YjSales
 *@version 4.0.0
 */
class GuideModel(application: Application) : BaseViewModel<GuideRepository>(application) {
    fun loading() {
        JConstant.setHttpPostService()
       mRepository.init(this)
    }

    override fun onNext(result: RxResult?) {
        super.onNext(result)
    }

    override fun onError(e: ApiException?) {
        super.onError(e)
    }

}