package io.codewar.scarlet.infra.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class RespJsonSerializer extends StdSerializer<Resp<?>> {

    public RespJsonSerializer() {
        this(null);
    }
    public RespJsonSerializer(Class<Resp<?>> t) {
        super(t);
    }

    @Override
    public void serialize(Resp<?> resp, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        switch (resp) {
            case Resp.Error error -> {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeNumberField("err_code", error.getCode());
                jsonGenerator.writeStringField("msg", error.getMsg());
                jsonGenerator.writeEndObject();
            }
            case Resp.Result result -> {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeNumberField("err_code", result.getCode());
                jsonGenerator.writeStringField("msg", result.getMsg());
                jsonGenerator.writeObjectField("results", result.getData());
                jsonGenerator.writeEndObject();
            }
            case Resp.PagerResult pager -> {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeNumberField("err_code", pager.getCode());
                jsonGenerator.writeStringField("msg", pager.getMsg());
                jsonGenerator.writeNumberField("total", pager.getTotal());
                jsonGenerator.writeObjectField("results", pager.getData());
                jsonGenerator.writeEndObject();
            }
        }
    }
}
