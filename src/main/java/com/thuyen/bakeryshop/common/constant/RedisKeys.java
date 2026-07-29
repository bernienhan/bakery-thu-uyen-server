package com.thuyen.bakeryshop.common.constant;

public final class RedisKeys {
    private RedisKeys() {}

    private static final String APP = "bakery-shop";

    public static final class Auth {
        private Auth() {}

        private static final String MODULE = "auth";

        public static String session(String sessionId) {
            return APP + ":" + MODULE + ":session:" + sessionId;
        }

        public static String userSessions(String userId) {
            return APP + ":" + MODULE + ":user:" + userId + ":sessions";
        }
    }
}
