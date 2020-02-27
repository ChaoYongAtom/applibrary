package com.androidlibrary.myapplication

import android.app.Application
import com.ruiyun.comm.library.live.BaseViewModel
import com.ruiyun.comm.library.live.RxResult
import com.ruiyun.comm.library.live.interfaces.CallBack
import com.ruiyun.comm.library.utils.UpdateApkUtil
import com.wcy.app.lib.dex.FixDexLoad
import com.wcy.app.lib.network.exception.ApiException
import com.wcy.app.lib.update.VersionBean

/**
 *GuideViewModel
 *@auth wangchaoyong
 *@time 2019/1/20
 *@description  YjSales
 *@version 4.0.0
 */
class GuideModel(application: Application) : BaseViewModel<GuideRepository>(application) {

    fun loading() {
        UpdateApkUtil.Update(getApplication(), object : CallBack {
            override fun onNext(result: RxResult?) {
            }
            override fun onError(e: ApiException?) {
                loadState.postValue(getStateError(1, "服务器连接失败，请稍后再试！"))
            }

        })
    }

    fun login() {
        mRepository.login(this)
    }

}