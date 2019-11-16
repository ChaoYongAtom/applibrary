package com.androidlibrary.myapplication.andfix

import android.content.Context
import android.widget.Toast

/**
 *KotlinBug
 *@auth wangchaoyong
 *@time 2019/11/7
 *@description  applibrary
 *@version 4.0.0
 */
class KotlinBug {
    companion object{
        fun  show( context: Context){
            //恭喜Kotlinbug修复！ 这是Kotlin一个优美的bug！
            Toast.makeText(context, "这是Kotlin一个优美的bug！", Toast.LENGTH_SHORT).show()
        }
    }

}