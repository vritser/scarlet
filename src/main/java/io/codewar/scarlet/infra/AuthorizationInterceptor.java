package io.codewar.scarlet.infra;

import io.codewar.scarlet.infra.annotation.PassAuth;
import io.codewar.scarlet.infra.model.AuthorizedUser;
import io.codewar.scarlet.infra.model.ErrorCode;
import io.codewar.scarlet.infra.model.RequestContext;
import io.codewar.scarlet.infra.model.Resp;
import io.codewar.scarlet.utils.JsonUtil;
import io.codewar.scarlet.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;

/**
 * AuthorizationInterceptor
 */
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (handler instanceof HandlerMethod) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");

            var method = ((HandlerMethod) handler);
            var passAuthAnnotation = method.getMethodAnnotation(PassAuth.class);
            if (passAuthAnnotation == null) {
                var token = request.getHeader("Authorization");

                if (Strings.isBlank(token)) {
                    try (PrintWriter out = response.getWriter()) {
                        JsonUtil.stringify(Resp.fail(ErrorCode.Unauthorized))
                                .forEach(out::write);
                    }
                    return false;
                }

                var result = JwtUtil.verify(token, "")
                        .map(claims -> new AuthorizedUser().setId(claims.get("id", Long.class)))
                        .map(RequestContext::setUser);

                if (result.isLeft()) {
                    try (PrintWriter out = response.getWriter()) {
                        JsonUtil.stringify(Resp.fail(result.getLeft()))
                                .forEach(out::write);
                    }
                    return false;
                }
            }
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        RequestContext.clear();
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
