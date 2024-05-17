package io.codewar.scarlet.infra;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import io.codewar.scarlet.infra.model.Resp;
import io.codewar.scarlet.infra.model.ErrorCode;
import io.vavr.control.Either;

@RestControllerAdvice
public class RespAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {

        if (body == null) return Resp.fail(ErrorCode.BadRequest);

        return switch (body) {
            case Resp<?> resp -> resp;
            case Either<?, ?> either -> ((Either<ErrorCode, ?>)either).fold(Resp::fail, Resp::succeed);
            default -> body;
        };
    }
}
