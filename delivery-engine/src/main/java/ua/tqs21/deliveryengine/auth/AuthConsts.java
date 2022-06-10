package ua.tqs21.deliveryengine.auth;

public class AuthConsts {

    // JWT PARAMS
    public static final String SECRET = "TESTE";
    public static final long EXPIRATION = 900_000 * 60;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_KEY = "Authorization";
    public static final String JWT_ROLE_CLAIM = "ROLE";

    // ROLE STRING REPRESENTATION
    public static final String SHOPPING_MANAGER = "ROLE_SHOPPING_MANAGER";
    public static final String STORE_MANAGER = "ROLE_STORE_MANAGER";

    // ENDPOINT MANAGEMENT
    public static final String LOG_IN_URL = "/auth/login";

    public static final String[] PUBLIC_ENDPOINTS = {
        "/user/**",
        "/courier/**",
        "/shop/**"
    };
}
