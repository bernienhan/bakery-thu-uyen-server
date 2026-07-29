package com.thuyen.bakeryshop.common.constant;

public final class ApiV1Paths {
    private ApiV1Paths() {}

    public static final String BASE = "/api/v1";

    public static final class Auth {
        private Auth() {}

        public static final String BASE = ApiV1Paths.BASE + "/auth";

        public static final String REGISTER = "/register";
        public static final String LOGIN = "/login";
        public static final String LOGOUT = "/logout";
        public static final String LOGOUT_ALL = "/logout-all";
        public static final String ME = "/me";
        public static final String SESSION = "/session";
    }

    public static final class Verification {
        private Verification() {}

        public static final String BASE = Auth.BASE + "/verification";

        public static final String SEND = "/send";
        public static final String CONFIRM = "/confirm";
        public static final String RESEND = "/resend";
    }

    public static final class Password {
        private Password() {}

        public static final String BASE = Auth.BASE + "/password";

        public static final String FORGOT = "/forgot";
        public static final String RESET = "/reset";
        public static final String CHANGE = "";
    }

    public static final class OAuth {
        private OAuth() {}

        public static final String BASE = Auth.BASE + "/oauth";

        public static final class Google {
            private Google() {}

            public static final String BASE = OAuth.BASE + "/google";

            public static final String AUTHORIZE = "";
            public static final String CALLBACK = "/callback";
            public static final String TOKEN = "/token";
        }
    }

    public static final class Sessions {
        private Sessions() {}

        public static final String BASE = Auth.BASE + "/sessions";

        public static final String BY_ID = "/{sessionId}";
    }
}