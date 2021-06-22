package it.buniva.strage.constant;

public class SecurityConstant {

    //    public static final long EXPIRATION_TIME = 1_800_000; // 30 minutes, expressed in milliseconds
//    public static final long EXPIRATION_TIME = 30_000; // 30 minutes, expressed in milliseconds

    public static final long EXPIRATION_TIME = 86_400_000; // 1 week, expressed in milliseconds

//    public static final String ANONYMOUS_USERNAME = "anonymousUser";

    public static final String TOKEN_PREFIX = "Bearer "; // Mean I can trust the token without verifier the provenience

    public static final String JWT_TOKEN_HEADER = "Jwt-Token"; // Http header

    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token Cannot be verifier";

    public static final String GET_ORGANIZATION_WHO_ISSUE_TOKEN = "Buniva"; // Organization who issue the token

    public static final String STRAGE_APPLICATION_USER = "Strage Application User"; // For the audiences

    public static final String AUTHORITIES = "authorities";

    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";

    public static final String ACCESS_DENIED_MESSAGE = "You need to have permission to access this page";

    public static final String OPTION_HTTP_METHOD = "OPTION";

    public static final String[] PUBLIC_URLS = {
            "/",
            "/*.html",
            "/favicon.ico",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js",
            "/users/**",
            "**"
    };

//     public static final String[] PUBLIC_URLS = {
//            "/user/login",
//            "/user/register",
//            "/user/reset-password/**",
//            "/user/image/**"};

//     public static final String[] PUBLIC_URLS = {"**"};
}
