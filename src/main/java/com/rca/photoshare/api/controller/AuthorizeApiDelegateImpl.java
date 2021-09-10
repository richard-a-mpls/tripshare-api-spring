package com.rca.photoshare.api.controller;

import com.rca.photoshare.api.AuthorizeApiDelegate;
import com.rca.photoshare.api.model.AuthorizationRequest;
import com.rca.photoshare.api.model.AuthorizationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class AuthorizeApiDelegateImpl implements AuthorizeApiDelegate {
    @Override
    public ResponseEntity<AuthorizationResponse> authorize(AuthorizationRequest authorizationRequest) {
        AuthorizationResponse response = new AuthorizationResponse();
        if (!"12344312".equals(authorizationRequest.getIdentityToken())) {
            return ResponseEntity.status(401).body(response);
        }
        response.setApiToken("21344321");
        return ResponseEntity.ok(response);
    }


}
