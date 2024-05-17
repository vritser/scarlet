package io.codewar.scarlet.infra.extension;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.codewar.scarlet.infra.model.Range;

import java.util.Optional;

public class OptionalQueryWrapper<T> extends QueryWrapper<T> {
    public final OptionalQueryWrapper<T> when(boolean isTrue, Object val, String... columns) {
        if (!isTrue) return this;
        this.and(q -> {
            for (var column : columns) {
                q.eq(column, val).or();
            }
        });
        return this;
    }

    public final OptionalQueryWrapper<T> multiLike(Object val, String... columns) {
        return switch (val) {
            case Optional<?> opt -> opt.map(value -> {
                this.and(q -> {
                    for (var column : columns) {
                        q.like(column, value).or();
                    }
                });
                return this;
            }).orElse(this);
            default -> this;
        };
    }

    public final OptionalQueryWrapper<T> multiEq(Object val, String... columns) {
        return switch (val) {
            case Optional<?> opt -> opt.map(value -> {
                this.and(q -> {
                    for (var column : columns) {
                        q.eq(column, value).or();
                    }
                });
                return this;
            }).orElse(this);
            default -> this;
        };
    }

    public <A extends Range<?>> OptionalQueryWrapper<T> betweenG(String column, Optional<A> dateRange) {
        dateRange.ifPresent(date -> super.between(column, date.start(), date.end()));
        return this;
    }

    public OptionalQueryWrapper<T> eqG(String column, Object val) {
        return switch (val) {
            case Optional<?> opt -> opt.map(value -> {
                super.eq(column, value);
                return this;
            }).orElse(this);
            default -> this.eqG(column, Optional.of(val));
        };
    }

    @Override
    public QueryWrapper<T> eq(String column, Object val) {
        return switch (val) {
            case Optional<?> opt -> opt.map(value -> super.eq(column, value)).orElse(this);
            default -> super.eq(column, val);
        };
    }

    public OptionalQueryWrapper<T> likeG(String column, Object val) {
        return switch (val) {
            case Optional<?> opt -> opt.map(value -> {
                super.like(column, value);
                return this;
            }).orElse(this);
            default -> this.likeG(column, Optional.of(val));
        };
    }

    @Override
    public QueryWrapper<T> like(String column, Object val) {
        return switch (val) {
            case Optional<?> opt -> opt.map(value -> super.like(column, value)).orElse(this);
            default -> super.like(column, val);
        };
    }
}
