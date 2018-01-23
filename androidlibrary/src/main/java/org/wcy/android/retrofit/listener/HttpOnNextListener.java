package org.wcy.android.retrofit.listener;

import org.wcy.android.retrofit.Api.BaseApi;
import org.wcy.android.retrofit.exception.ApiException;

/**
 * 成功回调处理
 * @version v1.0
 * @date 2017/3/14
 * @auth wcy
 * @company 重庆锐云科技有限公司
 */
public interface  HttpOnNextListener {
    /**
     * 成功后回调方法
     */
   void onNext(BaseApi api, String result);
    /**
     * 失败
     * 失败或者错误方法
     * 自定义异常处理
     * @param e
     */
    void onError(ApiException e, String mothead);

}
