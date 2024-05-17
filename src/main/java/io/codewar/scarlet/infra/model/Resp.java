package io.codewar.scarlet.infra.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.codewar.scarlet.infra.model.ErrorCode;
import io.vavr.control.Either;
import lombok.Getter;

import java.util.List;

@Getter
@JsonSerialize(using = RespJsonSerializer.class)
public sealed abstract class Resp<T> permits Resp.Error, Resp.PagerResult, Resp.Result {
    protected int code = ErrorCode.Success.code();
    protected String msg = ErrorCode.Success.msg();

    @Getter
    public static final class Result<T> extends Resp<T> {
        private final T data;

        private Result(T data) {
            this.data = data;
        }
    }

    public static final class Error extends Resp {
        private Error(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    @Getter
    public static final class PagerResult<T extends List<?>> extends Resp<T> {
        private final long total;
        private final T data;

        private PagerResult(T items) {
            this(items, items.size());
        }

        private PagerResult(T items, long total) {
            this.total = total;
            this.data = items;
        }

    }

    public static <T> Resp<T> succeed(T data) {
        return switch (data) {
            case PagerResult<?> pagerResult -> (Resp<T>)pagerResult;
            case Result<?> result -> (Resp<T>)result;
            default -> new Result<>(data);
        };
    }

    public static Error fail(ErrorCode failed) {
        return new Error(failed.code(), failed.msg());
    }

    public static <T> Resp<T> from(Either<ErrorCode, T> either) {
        return either.fold(Resp::fail, Resp::succeed);
    }

    public static <T extends List<?>> PagerResult<T> of(T items) {
        return new PagerResult<>(items);
    }

    public static <T extends List<?>> Resp<T> of(T items, long total) {
        return new PagerResult<>(items, total);
    }

    public static <T> Resp<List<T>> from(IPage<T> page) {
        return new PagerResult<>(page.getRecords(), page.getTotal());
    }
}
