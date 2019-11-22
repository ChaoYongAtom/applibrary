package com.androidlibrary.myapplication

import android.os.Bundle
import com.androidlibrary.myapplication.andfix.KotlinBug
import com.ruiyun.comm.library.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_kotlin.*

class KotlinActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        btn_sumbit.setOnClickListener {
            KotlinBug.show(this)
        }
    }
}
