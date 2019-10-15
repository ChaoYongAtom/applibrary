package com.wcy.app.lib_network.exception;

/**
 * 运行时自定义错误信息
 * 自由添加错误，需要自己扩展
 * @version v1.0
 * @date 2017/3/14
 * @auth wcy
 * @company 重庆锐云科技有限公司
 */
public class HttpTimeException extends RuntimeException {
    public HttpTimeException(int resultCode) {
        super(getApiExceptionMessage(resultCode));
    }

    public HttpTimeException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 转换错误数据
     *
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code) {
        switch (code) {
            case CodeException.UNKNOWN_ERROR:
                return "错误：网络错误";
            case CodeException.LOGIN_OUTTIME:
                return "错误：登录过期";
            case CodeException.TAPEOUT:
                return "错误：被迫下线";
            default:
                return "错误：未知错误";
        }
    }
}

