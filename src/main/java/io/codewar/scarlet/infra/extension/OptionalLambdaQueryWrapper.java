package io.codewar.scarlet.infra.extension;

import java.util.Optional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.codewar.scarlet.infra.model.Range;

public class OptionalLambdaQueryWrapper<T> extends LambdaQueryWrapper<T> {
    @SafeVarargs
    public final OptionalLambdaQueryWrapper<T> when(boolean isTrue, Object val, SFunction<T, ?>... columns) {
        if (!isTrue) return this;
        this.and(q -> {
            for (SFunction<T, ?> column : columns) {
                q.eq(column, val).or();
            }
        });
        return this;
    }

    @SafeVarargs
    public final OptionalLambdaQueryWrapper<T> multiLike(Object val, SFunction<T, ?>... columns) {
        return switch (val) {
            case Optional<?> opt -> opt.map(value -> {
                this.and(q -> {
                    for (SFunction<T, ?> column : columns) {
                        q.like(column, value).or();
                    }
                });
                return this;
            }).orElse(this);
            default -> this;
        };
    }

    @SafeVarargs
    public final OptionalLambdaQueryWrapper<T> multiEq(Object val, SFunction<T, ?>... columns) {
        for (SFunction<T, ?> column : columns) {
            this.or().eq(column, val);
        }
        return this;
    }

    public <A extends Range<?>> OptionalLambdaQueryWrapper<T> betweenG(SFunction<T, ?> column, Optional<A> rangeOpt) {
        return rangeOpt.map(range -> {
            if (range.end() == null) {
                super.gt(column, range.start());
            } else {
                super.between(column, range.start(), range.end());
            }
            return this;
        }).orElse(this);
    }

    public OptionalLambdaQueryWrapper<T> eqG(SFunction<T, ?> column, Object val) {
        return switch (val) {
            case Optional<?> opt -> opt.map(value -> {
                super.eq(column, value);
                return this;
            }).orElse(this);
            default -> this.eqG(column, Optional.of(val));
        };
    }

    public OptionalLambdaQueryWrapper<T> likeG(SFunction<T, ?> column, Object val) {
        return switch (val) {
            case Optional<?> opt -> opt.map(value -> {
                super.like(column, value);
                return this;
            }).orElse(this);
            default -> this.likeG(column, Optional.of(val));
        };
    }

    @Override
    public LambdaQueryWrapper<T> eq(SFunction<T, ?> column, Object val) {
        return switch (val) {
            case Optional<?> opt -> opt.map(value -> super.eq(column, value)).orElse(this);
            default -> super.eq(column, val);
        };
    }

    @Override
    public LambdaQueryWrapper<T> like(SFunction<T, ?> column, Object val) {
        return switch (val) {
            case Optional<?> opt -> opt.map(value -> super.like(column, value)).orElse(this);
            default -> super.like(column, val);
        };
    }
}
