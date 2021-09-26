package com.rca.photoshare.api.authorization;

import com.rca.photoshare.api.utility.SystemConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizeB2CTokenTest {

    @Mock
    SystemConfig systemConfigMock;

    @InjectMocks
    AuthorizeB2CToken authorizeB2CToken;

    private TokenModel tokenModel;

    @Before
    public void before() {
        tokenModel = new TokenModel();
        tokenModel.setNotBefore(System.currentTimeMillis() / 1000);
        tokenModel.setExpires(System.currentTimeMillis() / 1000);
        tokenModel.setAudience("sample audience");
    }

    @Test
    public void testVerifyJwtAttributesNotBeforeFail() {
        tokenModel.setNotBefore(tokenModel.getNotBefore() + 1);
        try {
            authorizeB2CToken.verifyJwtAttributes(tokenModel);
            fail("Exception not before not thrown");
        } catch (Exception exception) {
            assertEquals("Not Before validation failed", exception.getMessage());
        }
    }

    @Test
    public void testVerifyJwtAttributesNotBeforeSuccess() {
        tokenModel.setNotBefore(tokenModel.getNotBefore() - 1);
        when(systemConfigMock.getConfiguration(anyString())).thenReturn("sample audience");
        try {
            authorizeB2CToken.verifyJwtAttributes(tokenModel);
        } catch (Exception exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    public void testVerifyJwtAttributesExpiresFail() {
        tokenModel.setNotBefore(tokenModel.getNotBefore() - 1);
        tokenModel.setExpires(tokenModel.getExpires() - 1);
        try {
            authorizeB2CToken.verifyJwtAttributes(tokenModel);
            fail("Exception expires not thrown");
        } catch (Exception exception) {
            assertEquals("Token Expiration validation failed", exception.getMessage());
        }
    }

    @Test
    public void testVerifyJwtAttributesExpiresSuccess() {
        tokenModel.setNotBefore(tokenModel.getNotBefore() - 1);
        tokenModel.setExpires(tokenModel.getExpires() + 1);
        when(systemConfigMock.getConfiguration(anyString())).thenReturn("sample audience");
        try {
            authorizeB2CToken.verifyJwtAttributes(tokenModel);

        } catch (Exception exception) {
            fail("Exception expires not thrown");
        }
    }

    @Test
    public void testVerifyJwtAudienceFail() {
        tokenModel.setNotBefore(tokenModel.getNotBefore() - 1);
        tokenModel.setExpires(tokenModel.getExpires() + 1);
        when(systemConfigMock.getConfiguration(anyString())).thenReturn("bad audience");
        try {
            authorizeB2CToken.verifyJwtAttributes(tokenModel);
            fail("Exception audience not thrown");
        } catch (Exception exception) {
            assertEquals("Audience validation failed", exception.getMessage());
        }
    }

    @Test
    public void testVerifyJwtAudienceSuccess() {
        tokenModel.setNotBefore(tokenModel.getNotBefore() - 1);
        tokenModel.setExpires(tokenModel.getExpires() + 1);
        when(systemConfigMock.getConfiguration(anyString())).thenReturn("sample audience");
        try {
            authorizeB2CToken.verifyJwtAttributes(tokenModel);
        } catch (Exception exception) {
            fail("Exception audience not thrown");
        }
    }
}
