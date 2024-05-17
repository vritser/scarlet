package io.codewar.scarlet.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.vavr.control.Try;

/**
 * JsonUtil
 */
@Component
public class JsonUtil {

    private static ObjectMapper _mapper;

    @Autowired
    private ObjectMapper mapper;

    @PostConstruct
    public void init() { _mapper = mapper; }

    public static Try<String> stringify(Object object) {
        return Try.of(() -> _mapper.writeValueAsString(object));
    }

    public static <T> Try<T> parse(String json, Class<T> valueType) {
        return Try.of(() -> _mapper.readValue(json, valueType));
    }

    public static <T> Try<T> parse(String json, TypeReference<T> valueTypeRef) {
        return Try.of(() -> _mapper.readValue(json, valueTypeRef));
    }

    public static <T> Try<T> parse(byte[] bytes, Class<T> valueType) {
        return Try.of(() -> _mapper.readValue(bytes, valueType));
    }

    public static <T> Try<T> parse(byte[] bytes, TypeReference<T> valueTypeRef) {
        return Try.of(() -> _mapper.readValue(bytes, valueTypeRef));
    }
}
