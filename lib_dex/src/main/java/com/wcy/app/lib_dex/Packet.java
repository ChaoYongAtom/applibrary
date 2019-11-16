package com.wcy.app.lib_dex;

import java.nio.charset.Charset;

/**
 * 数据包定义
 * 指令格式：0xFFFFFFFF,指令码由8位16进制整数构成,对应二进制32位
 */
public class Packet {
    //JSON字段,指令与响应
    public static final String CMD = "CMD";
    //JSON字段,版本版本
    public static final String VER = "VER";
    //JSON字段,数据
    public static final String DATA = "DATA";
    //JSON字段,签名
    public static final String SIGN = "SIGN";
    //JSON字段,消息
    public static final String MSG = "MSG";
    public static final Charset UTF_8 = Charset.forName("UTF-8");
}
