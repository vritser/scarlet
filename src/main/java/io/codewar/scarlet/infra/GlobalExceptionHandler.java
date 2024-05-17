package io.codewar.scarlet.infra;

import java.util.HashMap;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.codewar.scarlet.infra.model.Resp;
import io.codewar.scarlet.infra.model.ErrorCode;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ ConvertFailedException.class, MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> general() {
        var res = Resp.fail(ErrorCode.BadRequest);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var res = Resp.fail(ErrorCode.BadRequest);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errMsg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        var res = new HashMap<>() {
            {
                put("err_code", 400);
                put("msg", errMsg);
            }
        };
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var res = Resp.fail(ErrorCode.BadRequest);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        return new ResponseEntity<>(Resp.fail(ErrorCode.PermissionDenied), HttpStatus.OK);
    }

}
