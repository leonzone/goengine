package com.gouuse.goengine.http.subscriber;


import com.gouuse.goengine.http.callback.ApiCallback;


/**
 * Created by reiserx on 2018/3/29.
 * desc :包含下载进度回调的订阅者
 */
public class DownCallbackSubscriber<T> extends ApiCallbackSubscriber<T> {
    public DownCallbackSubscriber(ApiCallback<T> callBack) {
        super(callBack);
    }

    @Override
    public void onComplete() {
        super.onComplete();
        callBack.onSuccess(super.data);
    }
}
