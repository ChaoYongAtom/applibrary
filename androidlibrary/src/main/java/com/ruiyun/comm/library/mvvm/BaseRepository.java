package com.ruiyun.comm.library.mvvm;

import com.alibaba.fastjson.JSONObject;

public abstract class BaseRepository extends AbsRepository<RxResult> {
    public JSONObject getJsonObject(){
        return new JSONObject();
    }
}
