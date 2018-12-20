
package com.xxyp.xxyp.common.service;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Description : Retrofit解析 Created by sunpengfei on 2017/7/27. Person in charge
 * : sunpengfei
 */
public final class LenientGsonConverterFactory extends Converter.Factory {

    private final Gson gson;

    private LenientGsonConverterFactory(Gson paramGson) {
        if (paramGson == null) {
            throw new NullPointerException("gson == null");
        }
        this.gson = paramGson;
    }

    public static LenientGsonConverterFactory create() {
        return create(new Gson());
    }

    public static LenientGsonConverterFactory create(Gson paramGson) {
        return new LenientGsonConverterFactory(paramGson);
    }

    public Converter<?, RequestBody> requestBodyConverter(Type paramType,
                                                          Annotation[] paramArrayOfAnnotation1, Annotation[] paramArrayOfAnnotation2,
                                                          Retrofit paramRetrofit) {
        TypeAdapter adapter = this.gson.getAdapter(TypeToken.get(paramType));
        return new LenientGsonRequestBodyConverter(adapter);
    }

    public Converter<ResponseBody, ?> responseBodyConverter(Type paramType,
            Annotation[] paramArrayOfAnnotation, Retrofit paramRetrofit) {
        TypeAdapter adapter = this.gson.getAdapter(TypeToken.get(paramType));
        return new LenientGsonResponseBodyConverter(adapter);
    }

    private class LenientGsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

        private final Charset UTF_8 = Charset.forName("UTF-8");

        private final TypeAdapter<T> adapter;

        private final Gson gson;

        LenientGsonRequestBodyConverter(TypeAdapter<T> typeAdapter) {
            this.gson = new Gson();
            adapter = typeAdapter;
        }

        public RequestBody convert(T t) throws IOException {
            Buffer localBuffer = new Buffer();
            OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(
                    localBuffer.outputStream(), this.UTF_8);
            JsonWriter localJsonWriter = this.gson.newJsonWriter(localOutputStreamWriter);
            localJsonWriter.setLenient(true);
            this.adapter.write(localJsonWriter, t);
            localJsonWriter.close();
            return RequestBody.create(this.MEDIA_TYPE, localBuffer.readByteString());
        }
    }

    private class LenientGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final TypeAdapter<T> adapter;

        private final Gson gson;

        LenientGsonResponseBodyConverter(TypeAdapter<T> adapter) {
            this.gson = new Gson();
            this.adapter = adapter;
        }

        public T convert(ResponseBody responseBody) throws IOException {
            JsonReader localJsonReader = this.gson.newJsonReader(responseBody.charStream());
            localJsonReader.setLenient(true);
            try {
                Object localObject2 = this.adapter.read(localJsonReader);
                return (T)localObject2;
            } finally {
                responseBody.close();
            }
        }
    }
}
