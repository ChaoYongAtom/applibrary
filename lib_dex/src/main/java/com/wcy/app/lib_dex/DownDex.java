package com.wcy.app.lib_dex;

import android.util.Base64;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wcy.app.lib.network.HttpBuilder;
import com.wcy.app.lib.network.HttpUtils;
import com.wcy.app.lib.network.interfaces.DownLoadResult;
import com.wcy.app.lib.network.interfaces.NetWorkResult;

import java.io.File;

/**
 * RxHttp
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/10/14
 * @description applibrary
 */
public class DownDex {
    private static final String TAG = "DownDex";

    public static void post(String key) {
        // 0x3A810201
        post(key, 0x3A810200);
    }

    private static void post(String key, int menty) {
        HttpBuilder httpBuilder = HttpBuilder.getBuilder("service");
        httpBuilder.setDomainUrl("http://www.yejay.cn/web-port/");
        //httpBuilder.setDomainUrl("http://172.16.2.188:8080/web-port/");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Packet.CMD, menty);
        jsonObject.put(Packet.VER, 1);
        jsonObject.put(Packet.SIGN, key.getBytes());
        JSONObject data = new JSONObject();
        data.put("VERSION", FixDexUtil.getInstance().getAppVersionCode());
        data.put("KEY", key);
        data.put("PACKAGENAME", FixDexUtil.getInstance().getPackageName());
        jsonObject.put(Packet.DATA, data.toJSONString().getBytes());
        HttpUtils.postBody(httpBuilder, encode(jsonObject.toJSONString()), new NetWorkResult() {
            @Override
            public void onNext(String result) {
                if (0x3A810200 == menty) {
                    JSONObject resAllJSON = JSONObject.parseObject(decode(result));
                    //获取返回消息
                    int cmdRes = resAllJSON.getIntValue(Packet.CMD);
                    if ((cmdRes & 0x000000FF) != 0x00000000) {
                    } else {
                        //数据
                        byte[] data = resAllJSON.getBytes(Packet.DATA);
                        JSONObject dataJson = toByteJSON(data);
                        downDex(dataJson.getString("url"), dataJson.getIntValue("ver"), dataJson.getString("name"), key);
                    }
                }

            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "原始数据+ 请求数据失败");
            }
        });
    }

    /**
     * 字节数组转JSON对象
     *
     * @param bytes
     * @return
     */
    private static JSONObject toByteJSON(byte[] bytes) {
        return JSON.parseObject(new String(bytes, Packet.UTF_8));
    }

    private static String decode(String data) {
        String strBase64 = new String(Base64.decode(data, Base64.DEFAULT));
        return strBase64;
    }

    private static String encode(String data) {
        String strBase64 = Base64.encodeToString(data.getBytes(), Base64.DEFAULT);
        return strBase64;
    }

    public static void downDex(String path, int ver, final String fileName, String key) {
        File file = new File(FixDexUtil.getInstance().getDexPaht(), fileName);
        if (!file.exists() || FixDexUtil.getInstance().isLatestVersion(ver)) {
            HttpUtils.download(path, file.getPath(), new DownLoadResult() {
                @Override
                public void onNext(String result) {
                    FixDexUtil.getInstance().loadFixedDex(fileName);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void Progress(int progress, long currentSize, long totalSize) {
                    if (progress == 100) {
                        Log.i(TAG, "文件下载成功");
                        post(key, 0x3A810201);
                    }
                }
            });
        } else {
            Log.i(TAG, "暂无更新文件");
            FixDexUtil.getInstance().loadFixedDex(fileName);
        }

    }

}
