package io.codewar.scarlet.infra;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import io.codewar.scarlet.infra.annotation.Authorized;
import io.codewar.scarlet.infra.annotation.QueryParams;
import io.codewar.scarlet.infra.model.DateRange;
import io.codewar.scarlet.infra.model.RankRange;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import io.codewar.scarlet.infra.model.RequestContext;

public class QueryParamsResolver implements HandlerMethodArgumentResolver {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(QueryParams.class) != null
                || parameter.getParameterAnnotation(Authorized.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        var authorizedAnnotation = parameter.getParameterAnnotation(Authorized.class);
        if (authorizedAnnotation != null) {
            return RequestContext.getUser();
        }

        var targetClass = parameter.getParameterType();
        var target = targetClass.getConstructor().newInstance();
        var wrapper = PropertyAccessorFactory.forBeanPropertyAccess(target);

        var fields = targetClass.getDeclaredFields();
        var superFields = targetClass.getSuperclass().getDeclaredFields();
        var allFields = new ArrayList<Field>() {
            {
                addAll(Arrays.asList(fields));
                addAll(Arrays.asList(superFields));
            }
        };

        for (Field field : allFields) {
            var paramName = this.camelToSnake(field.getName());
            var aliasAnnotation = field.getAnnotation(QueryParams.class);
            if (aliasAnnotation != null) {
                paramName = aliasAnnotation.value();
            }
            var value = webRequest.getParameter(paramName);
            if (value != null) {
                if (field.getType() == Optional.class) {
                    var type = field.getGenericType();
                    if (type instanceof ParameterizedType pt) {
                        for (var arg : pt.getActualTypeArguments()) {
                            if (DateRange.class == arg) {
                                var ary = value.split("\\s?to\\s?");
                                if (ary.length == 2) {
                                    var start = parseDate(ary[0], true);
                                    var end = parseDate(ary[1], false);
                                    wrapper.setPropertyValue(field.getName(), Optional.of(new DateRange(start, end)));
                                }
                            } else if (RankRange.class == arg) {
                                var ary = value.split("-");
                                if (ary.length > 0) {
                                    var start = Integer.parseInt(ary[0]);
                                    var end = ary.length == 2 ? Integer.parseInt(ary[1]) : null;
                                    wrapper.setPropertyValue(field.getName(), Optional.of(new RankRange(start, end)));
                                }
                            } else {
                                wrapper.setPropertyValue(field.getName(), Optional.ofNullable(value));
                            }
                        }
                        continue;
                    }
                }
                wrapper.setPropertyValue(field.getName(), value);
            }
        }

        return target;
    }

    public String camelToSnake(String str) {
        // Regular Expression
        String regex = "([a-z])([A-Z]+)";

        // Replacement string
        String replacement = "$1_$2";

        // Replace the given regex
        // with replacement string
        // and convert it to lower case.
        str = str.replaceAll(regex, replacement).toLowerCase();

        // return string
        return str;
    }

    private LocalDateTime parseDate(String date, boolean start) {
        var onlyDate = Pattern.compile("\\d{4}-\\d{2}-\\d{2}")
                .matcher(date)
                .matches();
        if (onlyDate) {
            var localDate = LocalDate.parse(date);
            if (start) return localDate.atStartOfDay();
            // return LocalTime.MAX.atDate(localDate);
            return LocalTime.of(23, 59, 59).atDate(localDate);
        }
        return LocalDateTime.parse(date, formatter);
    }
}
