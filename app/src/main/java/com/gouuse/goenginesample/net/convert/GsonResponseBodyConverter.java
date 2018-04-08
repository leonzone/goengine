package com.gouuse.goenginesample.net.convert;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.gouuse.goenginesample.engine.HttpStatus;
import com.gouuse.goenginesample.utils.ResponseHelper;

import java.io.IOException;
import java.net.UnknownServiceException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by reiserx on 2017/5/24.
 * des:自定义retrofit返回解析
 *
 * @param <T> 解析后返回的实体
 */
final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(@NonNull ResponseBody value) throws IOException {
        if (adapter != null && gson != null) {
            JsonReader jsonReader = gson.newJsonReader(value.charStream());
            try {
                T data = adapter.read(jsonReader);
                if (data == null) throw new UnknownServiceException("server back data is null");
                if (data instanceof HttpStatus) {
                    HttpStatus apiResult = (HttpStatus) data;
                    if (!ResponseHelper.isSuccess(apiResult)) {
                        throw new UnknownServiceException(apiResult.getMessage() == null ? "unknow error" : apiResult.getMessage());
                    }
                }
                return data;
            } finally {
                value.close();
            }
        } else {
            return null;
        }
    }
}
