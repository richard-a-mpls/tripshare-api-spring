package com.rca.photoshare.api.authorization;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenUtil {

    static String JWT_SECRET = "JWT_SECRET";

    public static String createJWT(String issuer, String subject, long ttlMillis) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Base64.getEncoder().encodeToString(System.getenv(JWT_SECRET).getBytes()));
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        Date now = new Date(System.currentTimeMillis());
        Date expires = new Date(System.currentTimeMillis() + ttlMillis);

        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setNotBefore(now)
                .setId(java.util.UUID.randomUUID().toString())
                .setIssuer(issuer)
                .setSubject(subject)
                .setExpiration(expires)
                .signWith(signatureAlgorithm, signingKey);


        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public static Claims decodeJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(
                        Base64.getEncoder().encodeToString(System.getenv(JWT_SECRET).getBytes())))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

}
