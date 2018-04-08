package com.gouuse.goenginesample.net;

import com.gouuse.goengine.http.callback.ApiCallback;

/**
 * Created by reiserx on 2018/4/3.
 * desc :错误回调
 */
public abstract class RequestCallBack<M> extends ApiCallback<M> {

    @Override
    public void onFail(int errCode, String errMsg) {
        onFailed(errCode, errMsg);
    }

    protected abstract void onFailed(int errCode, String errMsg);
}
