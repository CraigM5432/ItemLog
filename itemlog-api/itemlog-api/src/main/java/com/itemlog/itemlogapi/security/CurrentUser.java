package com.itemlog.itemlogapi.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

public class CurrentUser {

    private CurrentUser() {}

    public static Integer id() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getDetails() == null) return null;

        Object details = auth.getDetails();
        if (details instanceof Map<?, ?> map) {
            Object userId = map.get("userId");
            if (userId instanceof Integer i) return i;
            if (userId instanceof Number n) return n.intValue();
            if (userId != null) return Integer.valueOf(String.valueOf(userId));
        }
        return null;
    }
}

