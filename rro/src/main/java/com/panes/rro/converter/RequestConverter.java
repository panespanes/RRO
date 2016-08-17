package com.panes.rro.converter;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by panes on 2016/8/17.
 */
public class RequestConverter <T> implements Converter<T, RequestBody> {

    @Override
    public RequestBody convert(T value) throws IOException {
        return RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), JSON.toJSONBytes(value));
    }
}
