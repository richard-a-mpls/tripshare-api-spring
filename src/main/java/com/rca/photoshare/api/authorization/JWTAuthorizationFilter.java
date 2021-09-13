package com.rca.photoshare.api.authorization;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthorizationFilter extends GenericFilterBean {
    String HEADER_STRING = "Authorization";
    String TOKEN_PREFIX = "Bearer ";

    AuthorizeToken authorizeToken;

    public JWTAuthorizationFilter(AuthorizeToken authorizeToken) {
        this.authorizeToken = authorizeToken;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication((HttpServletRequest) servletRequest);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    // Reads the JWT from the Authorization header, and then uses JWT to validate the token
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);

        if (token != null) {
            try {
                String jwt = token.replace(TOKEN_PREFIX, "");
                TokenModel tokenModel = authorizeToken.authorizeToken(jwt);
                return new UsernamePasswordAuthenticationToken(tokenModel, null, new ArrayList<>());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }
}