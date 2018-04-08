package com.gouuse.goengine.http.request;


import com.gouuse.goengine.http.GoHttp;
import com.gouuse.goengine.http.callback.ApiCallback;
import com.gouuse.goengine.http.core.ApiManager;
import com.gouuse.goengine.http.mode.CacheResult;
import com.gouuse.goengine.http.subscriber.ApiCallbackSubscriber;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


/**
 * Created by reiserx on 2018/3/29.
 * desc :Options请求
 */
public class OptionsRequest extends BaseHttpRequest<OptionsRequest> {
    public OptionsRequest(String suffixUrl) {
        super(suffixUrl);
    }

    @Override
    protected <T> Observable<T> execute(Type type) {
        return apiService.options(suffixUrl, params).compose(this.<T>norTransformer(type));
    }

    @Override
    protected <T> Observable<CacheResult<T>> cacheExecute(Type type) {
        return this.<T>execute(type).compose(GoHttp.getApiCache().<T>transformer(cacheMode, type));
    }

    @Override
    protected <T> void execute(ApiCallback<T> callback) {
        DisposableObserver disposableObserver = new ApiCallbackSubscriber(callback);
        if (super.tag != null) {
            ApiManager.get().add(super.tag, disposableObserver);
        }
        if (isLocalCache) {
            this.cacheExecute(getSubType(callback)).subscribe(disposableObserver);
        } else {
            this.execute(getType(callback)).subscribe(disposableObserver);
        }
    }
}
