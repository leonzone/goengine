package com.gouuse.goengine.http.subscriber;


import com.gouuse.goengine.http.callback.ApiCallback;
import com.gouuse.goengine.http.exception.ApiException;


/**
 * Created by reiserx on 2018/3/29.
 * desc :包含回调的订阅者，如果订阅这个，上层在不使用订阅者的情况下可获得回调
 */
public class ApiCallbackSubscriber<T> extends ApiSubscriber<T> {

    ApiCallback<T> callBack;
    T data;

    public ApiCallbackSubscriber(ApiCallback<T> callBack) {
        if (callBack == null) {
            throw new NullPointerException("this callback is null!");
        }
        this.callBack = callBack;
    }

    @Override
    public void onError(ApiException e) {
        if (e == null) {
            callBack.onFail(-1, "This ApiException is Null.");
            return;
        }
        callBack.onFail(e.getCode(), e.getMessage());
    }

    @Override
    public void onNext(T t) {
        this.data = t;
        callBack.onSuccess(t);
    }

    @Override
    public void onComplete() {
    }

    public T getData() {
        return data;
    }
}
