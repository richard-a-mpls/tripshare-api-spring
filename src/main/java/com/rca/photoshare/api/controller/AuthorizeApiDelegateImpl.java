package com.rca.photoshare.api.controller;

import com.rca.photoshare.api.AuthorizeApiDelegate;
import com.rca.photoshare.api.authorization.AuthorizeToken;
import com.rca.photoshare.api.model.AuthorizationRequest;
import com.rca.photoshare.api.model.AuthorizationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeApiDelegateImpl implements AuthorizeApiDelegate {

    @Autowired
    AuthorizeToken authorizeToken;

    @Override
    public ResponseEntity<AuthorizationResponse> authorize(AuthorizationRequest authorizationRequest) {
        AuthorizationResponse response = new AuthorizationResponse();
        try {
            response.setApiToken(authorizeToken.authorizeToken(authorizationRequest.getIdentityToken()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }


}
