package com.rca.photoshare.api.authorization;

public interface AuthorizeToken {
    String authorizeToken(String token) throws Exception;
}
