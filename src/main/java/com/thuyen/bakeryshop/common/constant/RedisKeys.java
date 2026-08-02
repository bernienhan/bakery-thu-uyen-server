package com.thuyen.bakeryshop.common.constant;

public final class RedisKeys {
    private RedisKeys() {}

    private static final String APP = "bakery-shop";

    public static final class Auth {
        private Auth() {
        }

        private static final String MODULE = "auth";

        public static String session(String sessionId) {
            return APP + ":" + MODULE + ":session:" + sessionId;
        }

        public static String userSessions(String userId) {
            return APP + ":" + MODULE + ":user:" + userId + ":sessions";
        }

        public static final class RateLimit {
            private RateLimit() {
            }

            private static final String PURPOSE = "rate-limit";

            public static String loginIp(String ipAddress) {
                return APP + ":" + MODULE + ":" + PURPOSE + ":login:ip:" + ipAddress;
            }

            public static String loginIdentifier(String identifier) {
                return APP + ":" + MODULE + ":" + PURPOSE + ":login:identifier:" + identifier;
            }

            public static String registerIp(String ipAddress) {
                return APP + ":" + MODULE + ":" + PURPOSE + ":register:ip:" + ipAddress;
            }

            public static String forgotPasswordIdentifier(String identifier) {
                return APP + ":" + MODULE + ":" + PURPOSE + ":password-forgot:identifier:" + identifier;
            }

            public static String verificationSendIdentifier(String identifier) {
                return APP + ":" + MODULE + ":" + PURPOSE + ":verification-send:identifier:" + identifier;
            }

            public static String verificationConfirmIdentifier(String identifier) {
                return APP + ":" + MODULE + ":" + PURPOSE + ":verification-confirm:identifier:" + identifier;
            }
        }
    }
}
