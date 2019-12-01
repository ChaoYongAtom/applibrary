package com.wcy.app.lib_dex;

import java.nio.charset.Charset;

/**
 * 数据包定义
 */
public interface Packet {
    //JSON字段,指令与响应
    String CMD = "CMD";
    //JSON字段,版本版本
    String VER = "VER";
    //JSON字段,数据
    String DATA = "DATA";
    //JSON字段,签名
    String SIGN = "SIGN";
    //JSON字段,消息
    String MSG = "MSG";
    Charset UTF_8 = Charset.forName("UTF-8");
    String url = "url";
    String ver = "ver";
    String name = "name";
    String dexElements = "dexElements";
    String baseDexClassLoader = "BaseDexClassLoader";
    String pathList = "pathList";
}
