package com.androidlibrary.myapplication;

import android.util.Base64;

import com.alibaba.fastjson.JSONObject;

import org.junit.Test;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("CMD", 0x3A810200);
        jsonObject.put("VER", 1);
        JSONObject data = new JSONObject();
        data.put("version", 100);
        jsonObject.put("DATA", data.toJSONString().getBytes());
        String d = jsonObject.toJSONString();
        System.out.println(d);
        String e = Base64.encodeToString(d.getBytes(), Base64.DEFAULT);
        System.out.println(e);
        System.out.println(new String(Base64.decode(e, Base64.DEFAULT)));
    }
}