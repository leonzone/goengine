package com.gouuse.goengine.http.callback;

/**
 * Created by reiserx on 2018/3/29.
 * desc :请求接口回调
 */
public abstract class ApiCallback<T> {
    public abstract void onSuccess(T data);

    public abstract void onFail(int errCode, String errMsg);
}
