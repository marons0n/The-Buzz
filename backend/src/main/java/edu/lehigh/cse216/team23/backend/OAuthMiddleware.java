package edu.lehigh.cse216.team23.backend;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import java.util.Collections;

public class OAuthMiddleware implements Handler {
    private static final String CLIENT_ID = OAuthConfig.getClientId();

    @Override
    public void handle(Context ctx) throws Exception {
        String token = ctx.header("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new UnauthorizedResponse("Missing or invalid Authorization header");
        }

        token = token.substring("Bearer ".length());

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken = verifier.verify(token);
        if (idToken == null) {
            throw new UnauthorizedResponse("Invalid token");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String userId = payload.getSubject();  // User's unique ID
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        // Store user information in the context for use in the endpoint
        ctx.attribute("userId", userId);
        ctx.attribute("email", email);
        ctx.attribute("name", name);
    }
}
