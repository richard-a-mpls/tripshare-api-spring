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

    public String authorizeToken(String token) throws Exception {
        DecodedJWT decodedJWT = verifyJwtSignature(token);
        String jwtPayload = new String(Base64.getDecoder().decode(decodedJWT.getPayload()));

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        TokenModelB2C tokenModel = mapper.readValue(jwtPayload, TokenModelB2C.class);
        if (!verifyJwtAttributes(tokenModel)) {
            throw new Exception("Could not verify token attributes");
        }
        return null;
    }

    public DecodedJWT verifyJwtSignature(String sourceJwt) throws Exception {
        DecodedJWT decodedJWT = JWT.decode(sourceJwt);
        URL jwkUrl = new URL("https://rcaazdemo.b2clogin.com/rcaazdemo.onmicrosoft.com/discovery/v2.0/keys?p=b2c_1_susi");
        JwkProvider provider = new UrlJwkProvider(jwkUrl);
        Jwk jwk = provider.get(decodedJWT.getKeyId());
        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
        algorithm.verify(decodedJWT);
        return decodedJWT;
    }

    public boolean verifyJwtAttributes(TokenModelB2C tokenModel) {
        long currentTime = System.currentTimeMillis();
        if (tokenModel.getNotBefore() > currentTime) {
            System.out.println("issuing time failure");
            return false;
        }
        if (!System.getenv(AUDIENCE_ENV).equals(tokenModel.getAudience())) {
            System.out.println("wrong issuer");
            return false;
        }
        return true;
    }
}
