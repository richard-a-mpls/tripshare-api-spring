package com.rca.photoshare.api.authorization;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JWTAuthorizationFilterTest {

    @Mock
    HttpServletRequest requestMock;

    @Mock
    HttpServletResponse responseMock;

    @Mock
    FilterChain filterChain;

    @Mock
    AuthorizeToken authorizeTokenMock;

    @InjectMocks
    JWTAuthorizationFilter jwtAuthorizationFilter;

    static String BEARER = "Bearer";
    static String JWT = "FAKE SUCCESS JWT";

    @Test
    public void testDoFilterNullToken() {
        when(requestMock.getHeader(anyString())).thenReturn(null);
        try {
            jwtAuthorizationFilter.doFilter(requestMock, responseMock, filterChain);
        } catch (IOException | ServletException ex) {
            ex.printStackTrace();
        }
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testDoFilterBadToken() {
        when(requestMock.getHeader(anyString())).thenReturn(BEARER + " asdf");
        try {
            when(authorizeTokenMock.authorizeToken(anyString())).thenThrow(new Exception("The token was expected to have 3 parts, but got 1."));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            jwtAuthorizationFilter.doFilter(requestMock, responseMock, filterChain);
        } catch (IOException | ServletException ex) {
            ex.printStackTrace();
        }
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testDoFilterGoodToken() {
        TokenModel setupTokenModel = new TokenModel();
        setupTokenModel.setSubject("samplesubject");
        when(requestMock.getHeader(anyString()))
                .thenReturn(BEARER + " asdf")
                .thenReturn(BEARER + " " + JWT);
        try {
            when(authorizeTokenMock.authorizeToken(anyString()))
                    .thenReturn(setupTokenModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jwtAuthorizationFilter.doFilter(requestMock, responseMock, filterChain);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
        assertEquals("samplesubject",
                ((TokenModel)SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getSubject());
    }
}