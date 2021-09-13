package com.rca.photoshare.api.authorization;

public interface AuthorizeToken {
    TokenModel authorizeToken(String token) throws Exception;
}
