package com.rca.photoshare.api.database;

import com.rca.photoshare.api.authorization.TokenModel;
import com.rca.photoshare.api.model.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ProfileTemplate {

    @Autowired
    MongoTemplate mongoTemplate;

    public Profile lookupAuthenticatedProfile(Authentication authentication) {
        TokenModel tokenModel = (TokenModel)authentication.getPrincipal();
        Query query = new Query();
        query.addCriteria(Criteria.where("identity_id").is(tokenModel.getSubject())
                .and("identity_issuer").is(tokenModel.getIssuer()));
        return mongoTemplate.findOne(query, Profile.class);
    }
}
