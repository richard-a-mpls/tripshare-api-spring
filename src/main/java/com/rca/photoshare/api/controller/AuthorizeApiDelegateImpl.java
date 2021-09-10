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
            authorizeToken.authorizeToken(authorizationRequest.getIdentityToken());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body(response);
        }

        // return random uuid for now until we issue our own JWT.
        response.setApiToken(java.util.UUID.randomUUID().toString());
        return ResponseEntity.ok(response);
    }


}
