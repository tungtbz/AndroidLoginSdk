package com.bzsdk.bzloginmodule.network;

public class BZURL {
    public static final String GET_USER_INFO = "/api/v1/users/info";
    public static final String POST_SIGNUP_BY_PASS = "/api/v1/users/signup";
    public static final String POST_SIGNUP_REQUEST_OPT = "/api/v1/users/signup-email/request-otp";
    public static final String POST_SIGNUP_VALIDATE_OPT = "/api/v1/users/signup-email/validate-otp";

    public static final String POST_LOGIN_BY_PASS = "/api/v1/users/auth";
    public static final String POST_LOGIN_REQUEST_OPT = "/api/v1/users/auth-email/request-otp";
    public static final String POST_LOGIN_VALIDATE_OPT = "/api/v1/users/auth-email/validate-otp";

    public static final String POST_LOGIN_BY_WALLET = "/api/v1/users/auth/wallet";
    public static final String POST_LOGIN_BY_GOOGLE = "/api/v1/users/auth/google";
    public static final String POST_LOGIN_BY_FACEBOOK = "/api/v1/users/auth/facebook";

    public static final String POST_RECOVERY_PASSWORD_REQUEST_OPT = "/api/v1/users/recovery-password/request-otp";
    public static final String POST_RECOVERY_PASSWORD_VALIDATE_OPT = "/api/v1/users/recovery-password/validate-otp";
    public static final String PATCH_UPDATE_PASS = "/api/v1/users/password";
}
