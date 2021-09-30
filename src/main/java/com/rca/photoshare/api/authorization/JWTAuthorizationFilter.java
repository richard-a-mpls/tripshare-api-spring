package com.rca.photoshare.api.authorization;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthorizationFilter extends GenericFilterBean {
    String HEADER_STRING = "Authorization";
    String TOKEN_PREFIX = "Bearer ";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String header = req.getHeader(HEADER_STRING);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication((HttpServletRequest) servletRequest);

        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    // Reads the JWT from the Authorization header, and then uses JWT to validate the token
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            try {
                String jwt = token.replace(TOKEN_PREFIX, "");
                WebClient webClient = WebClient.create();
                ResponseEntity<TokenModel> tokenModelResponseEntity = webClient.get()
                        .uri(System.getenv("JWT_VERIFY_ENDPOINT"))
                        .header("Authorization", "Basic " + System.getenv("JWT_VERIFY_AUTH"))
                        .header("ContentType", "application/json")
                        .header("tokenJwt", jwt)
                        .retrieve()
                        .toEntity(TokenModel.class)
                        .block();

                if (HttpStatus.OK.equals(tokenModelResponseEntity.getStatusCode())) {
                    TokenModel tokenModel = tokenModelResponseEntity.getBody();
                    return new UsernamePasswordAuthenticationToken(tokenModel, null, new ArrayList<>());
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
        return null;
    }
}