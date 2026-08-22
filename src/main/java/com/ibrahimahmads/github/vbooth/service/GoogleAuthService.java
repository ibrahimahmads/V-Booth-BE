package com.ibrahimahmads.github.vbooth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public interface GoogleAuthService {
    GoogleIdToken.Payload verifyAndExtractToken(String idTokenString);
}
