package com.wcy.app.lib.dex;

import android.util.Base64;

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
    public static void post(String key, String deviceId) {
        // 0x3A810201
        post(key, 0x3A810200, deviceId);
    }

    public static void post(String key, int menty) {
        // 0x3A810201
        post(key, menty, null);
    }

    private static void post(String key, int menty, String deviceId) {
        try {


            HttpBuilder httpBuilder = HttpBuilder.getBuilder("service");
            httpBuilder.setDomainUrl("http://web.yejay.cn/port/");
            //httpBuilder.setDomainUrl("http://172.16.2.188:8080/web-port/");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Packet.CMD, menty);
            jsonObject.put(Packet.VER, 1);
            jsonObject.put(Packet.SIGN, key.getBytes());
            JSONObject data = new JSONObject();
            data.put("VERSION", FixDexUtil.getInstance().getAppVersionCode());
            data.put("KEY", key);
            data.put("PACKAGENAME", FixDexUtil.getInstance().getPackageName());
            if (deviceId != null) data.put("DEVICEID", deviceId);
            jsonObject.put(Packet.DATA, data.toJSONString().getBytes());
            httpBuilder.setTimeout(5);
            HttpUtils.postBody(httpBuilder, encode(jsonObject.toJSONString()), new NetWorkResult() {
                @Override
                public void onNext(String result) {
                    LogUtil.log("原始数据" + decode(result));
                    if (0x3A810200 == menty) {
                        JSONObject resAllJSON = JSONObject.parseObject(decode(result));
                        //获取返回消息
                        int cmdRes = resAllJSON.getIntValue(Packet.CMD);
                        if ((cmdRes & 0x000000FF) != 0x00000000) {
                            FixDexUtil.getInstance().loadFixedDex();
                        } else {
                            //数据
                            byte[] data = resAllJSON.getBytes(Packet.DATA);
                            JSONObject dataJson = toByteJSON(data);
                            LogUtil.log(dataJson.toJSONString());
                            downDex(dataJson.getString(Packet.url), dataJson.getIntValue(Packet.ver), dataJson.getString(Packet.name), key);
                        }
                    }

                }

                @Override
                public void onError(Throwable e) {
                    FixDexUtil.getInstance().loadFixedDex();
                    LogUtil.log("原始数据+ 请求数据失败");
                }
            });
        } catch (Exception e) {

        }
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

    private static void downDex(String path, int ver, final String fileName, String key) {
        File file = new File(FixDexUtil.getInstance().getJarPaht(), fileName);
        if (!file.exists() || FixDexUtil.getInstance().isLatestVersion(ver)) {
            FixDexUtil.getInstance().detaleJar();
            HttpUtils.download(path, file.getPath(), new DownLoadResult() {
                @Override
                public void onNext(String result) {
                    LogUtil.log("下载完成");
                    FixDexUtil.getInstance().loadFixedDex(fileName);
                    post(key, 0x3A810201);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void Progress(int progress, long currentSize, long totalSize) {
                }
            });
        } else {
            LogUtil.log("暂无更新文件");
            FixDexUtil.getInstance().loadFixedDex();
        }

    }

}
