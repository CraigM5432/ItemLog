package com.itemlog.itemlogapi.security;

public class TokenGuards {

    private TokenGuards() {}

    /**
     * Ensures the {userId} in the legacy URL matches the userId in the JWT.
     * Preventing malicious users from guessing other userIds, while ole routes are active
     */
    public static void requirePathUserMatchesToken(Integer pathUserId) {
        Integer tokenUserId = CurrentUser.id();

        if (tokenUserId == null) {
            throw new IllegalArgumentException("Not authenticated.");
        }

        if (pathUserId == null || !tokenUserId.equals(pathUserId)) {
            throw new IllegalArgumentException("Forbidden: userId does not match token.");
        }
    }
}
