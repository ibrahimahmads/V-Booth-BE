package com.ibrahimahmads.github.vbooth.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.ibrahimahmads.github.vbooth.service.GoogleAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {
    @Value("${google.client-id}")
    private String googleClientId;
    @Override
    public GoogleIdToken.Payload verifyAndExtractToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance()
            )
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                return idToken.getPayload();
            } else {
                throw new IllegalArgumentException("Google ID Token tidak valid.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Gagal memverifikasi token Google: " + e.getMessage());
        }
    }
}
