package edu.lehigh.cse216.team23.backend;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.gson.GsonFactory;

public class OAuthConfig {

    public static void verifyToken(String idTokenString, HttpTransport transport, JsonFactory jsonFactory, String CLIENT_ID) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
            .setAudience(Collections.singletonList(CLIENT_ID))
            .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            //System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            // boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            // String name = (String) payload.get("name");
            // String pictureUrl = (String) payload.get("picture");
            // String locale = (String) payload.get("locale");
            // String familyName = (String) payload.get("family_name");
            // String givenName = (String) payload.get("given_name");

            // Use or store profile information
            //System.out.println("Email: " + email);

            //print token
            //System.out.println("Token: " + idTokenString);

        } else {
            System.out.println("Invalid ID token.");
        }
        
    }

    public static String getClientId() {
        // print client id
        return System.getenv("CLIENT_ID");
    }

    public static String getClientSecret() {
        // print client secret
        return System.getenv("CLIENT_SECRET");
    }

    public static String getRedirectUri() {
        // print redirect uri
        return System.getenv("REDIRECT_URI");
    }
}