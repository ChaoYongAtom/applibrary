package org.wcy.android.retrofit.exception;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 自定义错误code类型:注解写法
 * <p>
 * 可自由扩展
 * @version v1.0
 * @date 2017/3/14
 * @auth wcy
 * @company 重庆锐云科技有限公司
 */

public class CodeException {

    /*网络错误*/
    public static final int NETWORD_ERROR = 0x1;
    /*http_错误*/
    public static final int HTTP_ERROR = 0x2;
    /*fastjson错误*/
    public static final int JSON_ERROR = 0x3;
    /*未知错误*/
    public static final int UNKNOWN_ERROR = 0x4;
    /*运行时异常-包含自定义异常*/
    public static final int RUNTIME_ERROR = 0x5;
    /*无法解析该域名*/
    public static final int UNKOWNHOST_ERROR = 0x6;
    /*登录过期*/
    public static final int LOGIN_OUTTIME = 0x7;
    /*被迫下线*/
    public static final int TAPEOUT = 0x8;
    public static  final int ERROR=0x9;
    public static final int NOT_NETWORD=0x10;

    @IntDef({NETWORD_ERROR, HTTP_ERROR, RUNTIME_ERROR, UNKNOWN_ERROR, JSON_ERROR, UNKOWNHOST_ERROR,LOGIN_OUTTIME,TAPEOUT,ERROR})
    @Retention(RetentionPolicy.SOURCE)

    public @interface CodeEp {
    }

}
