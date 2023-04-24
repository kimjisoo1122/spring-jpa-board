package com.example.shop.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public interface CookieUtil {

    static Cookie getCookie(HttpServletRequest request, String cookieName) {
        return Optional.ofNullable(request.getCookies()).stream()
                .flatMap(Arrays::stream)
                .filter(e -> Objects.equals(e.getName(), cookieName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 쿠키가 존재하지 않습니다"));
    }
}
