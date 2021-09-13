package com.rca.photoshare.api.authorization;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

@Component
public class AuthorizeB2CToken implements AuthorizeToken {

    String AUDIENCE_ENV = "B2C_AUDIENCE";
    String JWK_URL = "JWK_URL";

    public TokenModel authorizeToken(String token) throws Exception {
        DecodedJWT decodedJWT = verifyJwtSignature(token);
        String jwtPayload = new String(Base64.getDecoder().decode(decodedJWT.getPayload()));

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        TokenModel tokenModel = mapper.readValue(jwtPayload, TokenModel.class);
        verifyJwtAttributes(tokenModel);

        return tokenModel;
    }

    public DecodedJWT verifyJwtSignature(String sourceJwt) throws Exception {
        DecodedJWT decodedJWT = JWT.decode(sourceJwt);
        URL jwkUrl = new URL(System.getenv(JWK_URL));
        JwkProvider provider = new UrlJwkProvider(jwkUrl);
        Jwk jwk = provider.get(decodedJWT.getKeyId());
        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
        algorithm.verify(decodedJWT);
        return decodedJWT;
    }

    public void verifyJwtAttributes(TokenModel tokenModel) throws Exception {
        long currentTime = System.currentTimeMillis()/1000;
        if (tokenModel.getNotBefore() > currentTime) {
            throw new Exception("Not Before validation failed");
        }
        if (tokenModel.getExpires() < currentTime) {
            System.out.println(tokenModel.getExpires());
            System.out.println(currentTime);
            throw new Exception("Token Expiration validation failed");
        }
        if (!System.getenv(AUDIENCE_ENV).equals(tokenModel.getAudience())) {
            throw new Exception("Issuer validation failed");
        }
    }
}
