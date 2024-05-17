package io.codewar.scarlet.utils;

import io.codewar.scarlet.infra.model.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.vavr.control.Either;
import io.vavr.control.Try;

/**
 * JwtUtil
 */
public class JwtUtil {

    public static String authorize(Long userId, String key) {
        return Jwts.builder()
                .claim("id", userId)
                .signWith(Keys.hmacShaKeyFor(key.getBytes()))
                .compact();
    }

    public static Either<ErrorCode, Claims> verify(String token, String key) {
        return Try.of(() -> Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(key.getBytes()))
                        .build()
                        .parseClaimsJws(token)
                        .getBody())
                .toEither(ErrorCode.Unauthorized);
    }
}
